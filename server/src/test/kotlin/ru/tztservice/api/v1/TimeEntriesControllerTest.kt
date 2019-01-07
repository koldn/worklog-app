package ru.tztservice.api.v1

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.test.web.client.postForObject
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils
import ru.tztservice.BasicIntegrationTest
import ru.tztservice.domain.TimeEntry
import ru.tztservice.repositories.TimeEntriesRepository
import ru.tztservice.security.auth.JwtAuthConfiguration
import ru.tztservice.shared.CredentialsDto
import ru.tztservice.shared.TimeEntryDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class TimeEntriesControllerTest : BasicIntegrationTest() {

    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var jwtAuthConfiguration: JwtAuthConfiguration
    @Autowired
    lateinit var timeEntriesRepository: TimeEntriesRepository

    @BeforeAll
    internal fun setUp() {
        val creds = CredentialsDto(RandomStringUtils.random(10), RandomStringUtils.random(10))
        restTemplate.postForEntity<Void>("http://localhost:$port/user/register", creds)
        val authRsp = restTemplate.postForEntity<Void>("http://localhost:$port/auth/", creds)
        val headerValue = authRsp.headers.getFirst(jwtAuthConfiguration.header)
        restTemplate.restTemplate.interceptors.add(ClientHttpRequestInterceptor { request, body, execution ->
            request.headers[jwtAuthConfiguration.header] = headerValue
            execution.execute(request, body)
        })
    }

    @AfterEach
    internal fun clearEntries() = timeEntriesRepository.deleteAll()

    @Test
    internal fun `Test editing an existent time-entry`() {
        val save = timeEntriesRepository.save(TimeEntry(startTimestamp = Instant.now(), userID = -1))
        val end = Instant.now().plusMillis(1000)
        val timeEntryDto = TimeEntryDto(start = save.startTimestamp.toString(), entryID = save.id, end = end.toString())
        val savedEntryDto =
            restTemplate.postForObject<TimeEntryDto>("http://localhost:$port/api/v1/time_entries/save", timeEntryDto)
        assertEquals(end.toString(), savedEntryDto!!.end)
    }

    @Test
    internal fun `Getting time entries by date should be ordered by start desc`() {
        val firstEntry = restTemplate.postForObject<TimeEntryDto>(
            "http://localhost:$port/api/v1/time_entries/save",
            TimeEntryDto(start = Instant.now().toString())
        )
        val secondEntry = restTemplate.postForObject<TimeEntryDto>(
            "http://localhost:$port/api/v1/time_entries/save",
            TimeEntryDto(start = Instant.now().plusMillis(1000).toString())
        )

        val formattedDate =
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC")).format(Instant.now())

        val objects =
            restTemplate.getForObject<EntryList>("http://localhost:$port/api/v1/time_entries/get/$formattedDate")
        assertEquals(2, objects!!.size)
        assertEquals(secondEntry, objects[0])
        assertEquals(firstEntry, objects[1])
    }

    @Test
    internal fun `Test entries of one user is not shown to another user`() {
        timeEntriesRepository.save(TimeEntry(startTimestamp = Instant.now(), userID = -10))
        timeEntriesRepository.save(TimeEntry(startTimestamp = Instant.now(), userID = -10))

        val formattedDate =
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC")).format(Instant.now())

        val objects =
            restTemplate.getForObject<EntryList>("http://localhost:$port/api/v1/time_entries/get/$formattedDate")
        assertEquals(0, objects!!.size)
    }


    @Test
    internal fun `New entry gets an assigned ID`() {
        val timeEntryDto = TimeEntryDto(start = Instant.now().toString())
        val savedEntryDto =
            restTemplate.postForObject<TimeEntryDto>("http://localhost:$port/api/v1/time_entries/save", timeEntryDto)
        assertNotEquals(-1L, savedEntryDto!!.entryID)
    }

    @Test
    internal fun `Test entry successful deletion`() {
        val save = timeEntriesRepository.save(TimeEntry(startTimestamp = Instant.now(), userID = -1))
        val params = mapOf("entryID" to save.id)
        restTemplate.delete("http://localhost:$port/api/v1/time_entries/delete?entryID={entryID}", params)

        val findById = timeEntriesRepository.findById(save.id)
        assertFalse(findById.isPresent)
    }

    private class EntryList : MutableList<TimeEntryDto> by ArrayList()
}

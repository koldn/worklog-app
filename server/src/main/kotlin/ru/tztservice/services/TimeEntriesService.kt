package ru.tztservice.services

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.tztservice.domain.TimeEntry
import ru.tztservice.repositories.TimeEntriesRepository
import ru.tztservice.shared.TimeEntryDto
import java.time.*
import java.util.*

@Component
@Transactional
class TimeEntriesService(
    private val timeEntriesRepository: TimeEntriesRepository,
    private val userService: UserService
) {


    fun saveOrUpdate(timeEntryDto: TimeEntryDto): TimeEntry {

        val timeEntry = timeEntriesRepository.findById(timeEntryDto.entryID).orElseGet {
            val currentUser = SecurityContextHolder.getContext().authentication.name
            val userID = userService.getUserByLogin(currentUser).get().id
            TimeEntry(startTimestamp = Instant.parse(timeEntryDto.start), userID = userID)
        }
        Instant.now().toString()
        if (timeEntryDto.burnComment != null) timeEntry.burndownComment = timeEntryDto.burnComment!!
        if (timeEntryDto.shortDesc != null) timeEntry.shortDescription = timeEntryDto.shortDesc!!
        if (timeEntryDto.end != null) timeEntry.endTimestamp = Instant.parse(timeEntryDto.end)
        val save = timeEntriesRepository.save(timeEntry)
        return save
    }

    /**
     * Удалить запись, возвращается DTO от удаленной записи для возможности сделать UNDO
     */
    fun deleteTimeEntry(id: Long): TimeEntry {
        val deletedEntry = timeEntriesRepository.findById(id).get()
        timeEntriesRepository.deleteById(id)
        return deletedEntry
    }

    fun getEntriesByDate(date: LocalDate): Iterable<TimeEntry> {
        val midnight = date.atStartOfDay().toInstant(ZoneOffset.UTC)
        val endOfDay = date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)

        val currentUser = SecurityContextHolder.getContext().authentication.name
        val userID = userService.getUserByLogin(currentUser).get().id

        val timestampDesc =
            timeEntriesRepository.findTimeEntriesByUserIDAndStartTimestampBetweenOrderByStartTimestampDesc(
                userID,
                midnight,
                endOfDay
            )
        return timestampDesc
    }
}
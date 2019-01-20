package ru.worklog.auth

import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.RequestEntity
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.random
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import ru.worklog.BasicIntegrationTest
import ru.worklog.security.auth.JwtAuthConfiguration
import ru.worklog.shared.CredentialsDto
import java.net.URI

internal class AuthTest : BasicIntegrationTest() {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var jwtAuthConfiguration: JwtAuthConfiguration

    @Test
    @DisplayName("Check issued token")
    internal fun tokenShouldBeIssued() {

        val creds = CredentialsDto(random(10), random(10))
        val credentialsEntity = HttpEntity(creds)
        restTemplate.postForEntity("http://localhost:$port/user/register", credentialsEntity, String::class.java)
        val authResponse =
            restTemplate.postForEntity("http://localhost:$port/auth/", credentialsEntity, Nothing::class.java)
        val headerValue = authResponse.headers.getFirst(jwtAuthConfiguration.header)
        val token = headerValue!!.replace(jwtAuthConfiguration.tokenPrefix, "").trim()

        val claims = Jwts.parser().setSigningKey(jwtAuthConfiguration.secret).parseClaimsJws(token).body
        assertEquals(claims.subject, creds.userName)
    }

    @Test
    internal fun testTokenAuth() {
        val creds = CredentialsDto(randomAlphabetic(10), random(10))
        val credentialsEntity = HttpEntity(creds)
        restTemplate.postForEntity("http://localhost:$port/user/register", credentialsEntity, String::class.java)
        val authResponse =
            restTemplate.postForEntity("http://localhost:$port/auth/", credentialsEntity, Nothing::class.java)
        val headerValue = authResponse.headers.getFirst(jwtAuthConfiguration.header)

        val noAuth =
            restTemplate.getForEntity("http://localhost:$port/user/get/${creds.userName}", Nothing::class.java)

        Assertions.assertTrue(noAuth.statusCode.is4xxClientError)

        val requestEntity = RequestEntity.get(URI("http://localhost:$port/user/get/${creds.userName}"))
            .header(jwtAuthConfiguration.header, headerValue).build()
        val response = restTemplate.exchange(requestEntity, String::class.java)
        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
    }
}
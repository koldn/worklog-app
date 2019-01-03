package ru.tztservice.server.rest.api.v1.controllers.user

import org.junit.Assert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.random
import ru.tztservice.server.BasicIntegrationTest
import ru.tztservice.server.security.CredentialsDto

internal class UserControllerTest : BasicIntegrationTest() {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    @DisplayName("New user registration")
    fun testInternalRegistrationShouldWork() {
        val request = HttpEntity(CredentialsDto(random(10), random(10)))
        val postForEntity =
            restTemplate.postForEntity("http://localhost:$port/user/register", request, String::class.java)
        Assert.assertTrue(postForEntity.statusCode.is2xxSuccessful)
    }

    @Test
    @DisplayName("There is an authentication token in response headers")
    fun testAuthenticationToken(){
        val credentials = HttpEntity(CredentialsDto(random(10), random(10)))
        restTemplate.postForEntity("http://localhost:$port/user/register", credentials, String::class.java)
        val authResponse = restTemplate.postForEntity("http://localhost:$port/auth/", credentials, Nothing::class.java)
        val token = authResponse.headers.getFirst("token")
        Assert.assertEquals("123",token)
    }

}
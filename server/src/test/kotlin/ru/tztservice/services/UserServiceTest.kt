package ru.tztservice.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import ru.tztservice.BasicIntegrationTest

internal class UserServiceTest : BasicIntegrationTest() {

    @Autowired
    private lateinit var userService: UserService

    @Test
    @DisplayName("Creating and getting new user completes without errors")
    internal fun saveNewUser() {
        val userName = randomAlphabetic(10)
        val userPassword = randomAlphabetic(10)

        userService.saveNewUser(userName, userPassword)
        userService.getUserByLogin(userName)
    }

    @Test
    @DisplayName("Exception is thrown when user not found")
    internal fun exceptionOnUserNotFound() {
        Assertions.assertFalse(userService.getUserByLogin(randomAlphabetic(10)).isPresent)
    }
}
package ru.worklog.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.worklog.services.UserService
import ru.worklog.shared.CredentialsDto

@Controller
@RequestMapping("/user")
class UsersController(val userService: UserService) {
    @PostMapping("/register")
    fun registerNewUser(@RequestBody creds: CredentialsDto) {
        userService.saveNewUser(creds.userName, creds.userPassword)
    }
}
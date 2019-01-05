package ru.tztservice.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.tztservice.domain.DomainUser
import ru.tztservice.dto.CredentialsDto
import ru.tztservice.services.UserService
import java.lang.RuntimeException
import javax.print.attribute.standard.JobOriginatingUserName

@Controller
class UsersController(val userService: UserService) {
    @PostMapping("/user/register")
    fun registerNewUser(@RequestBody creds: CredentialsDto) {
        userService.saveNewUser(creds.userName, creds.userPassword)
    }

    //TODO : delete me when other controllers are done
    @GetMapping("/user/get/{userName}")
    @ResponseBody
    fun getUser(@PathVariable userName: String): String {
        return userName
    }
}
package ru.tztservice.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.tztservice.services.UserService
import ru.tztservice.shared.CredentialsDto

@Controller
@RequestMapping("/user")
class UsersController(val userService: UserService) {
    @PostMapping("/register")
    fun registerNewUser(@RequestBody creds: CredentialsDto) {
        userService.saveNewUser(creds.userName, creds.userPassword)
    }

    //TODO : delete me when other controllers are done
    @GetMapping("/get/{userName}")
    @ResponseBody
    fun getUser(@PathVariable userName: String): String {
        return userName
    }
}
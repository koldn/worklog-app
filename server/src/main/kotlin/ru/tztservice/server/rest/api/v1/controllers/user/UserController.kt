package ru.tztservice.server.rest.api.v1.controllers.user

import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.tztservice.server.services.UserService
import ru.tztservice.server.security.CredentialsDto

@RestController
class UserController(private val userService: UserService, private val passwordEncoder: PasswordEncoder) {

    @PostMapping("/user/register")
    fun register(@RequestBody credentialsDto: CredentialsDto): ResponseEntity<String> {
        userService.saveNewUser(credentialsDto.user,passwordEncoder.encode(credentialsDto.password))
        return ResponseEntity.ok().body(credentialsDto.user)
    }
}
package ru.tztservice.server.services

import org.springframework.stereotype.Component
import ru.tztservice.server.domain.DomainUser
import ru.tztservice.server.repostory.UserRepository
import java.lang.IllegalArgumentException
import java.util.*

@Component
class UserService(private val userRepository: UserRepository) {

    fun saveNewUser(userName: String, password: String): Boolean {
        if(!userRepository.existsById(userName)) {
            userRepository.save(DomainUser(userName, password))
        }
        return true
    }

    fun getUser(userName: String): DomainUser {
        return userRepository.findById(userName).orElseThrow { IllegalArgumentException()}
    }
}
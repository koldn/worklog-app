package ru.worklog.services

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.worklog.domain.DomainUser
import ru.worklog.repositories.DomainUserRepository
import java.util.*

@Component
@Transactional
class UserService(private val userRepository: DomainUserRepository, private val passwordEncoder: PasswordEncoder) {

    @Transactional(readOnly = true)
    fun getUserByLogin(userName: String): Optional<DomainUser> {
        return userRepository.findByUserName(userName)
    }

    fun saveNewUser(userName: String, password: String): Long {
        return userRepository.save(DomainUser(userName, passwordEncoder.encode(password))).id
    }

    fun isUserPresent(userName: String): Boolean {
        return userRepository.existsByUserName(userName)
    }

}
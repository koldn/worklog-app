package ru.tztservice.security.services

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.tztservice.services.UserService

@Service
class DomainUserDetailsService(val usersService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val userByLogin = usersService.getUserByLogin(username!!)
            .orElseThrow { UsernameNotFoundException("User $username not found") }
        return User(userByLogin.userName, userByLogin.password, listOf())
    }
}
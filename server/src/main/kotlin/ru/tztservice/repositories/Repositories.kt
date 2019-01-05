package ru.tztservice.repositories

import org.springframework.data.repository.CrudRepository
import ru.tztservice.domain.DomainUser
import java.util.*

/**
 * Репозиторий пользователей
 */
interface DomainUserRepository : CrudRepository<DomainUser, Long> {
    fun findByUserName(userName: String): Optional<DomainUser>

    fun existsByUserName(userName: String): Boolean
}
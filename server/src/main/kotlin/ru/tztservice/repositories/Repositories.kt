package ru.tztservice.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.tztservice.domain.DomainUser
import ru.tztservice.domain.TimeEntry
import java.time.Instant
import java.util.*

/**
 * Репозиторий пользователей
 */
interface DomainUserRepository : CrudRepository<DomainUser, Long> {
    fun findByUserName(userName: String): Optional<DomainUser>

    fun existsByUserName(userName: String): Boolean
}

interface TimeEntriesRepository : CrudRepository<TimeEntry, Long> {
    fun findTimeEntriesByUserIDAndStartTimestampBetweenOrderByStartTimestampDesc(
        userID: Long,
        from: Instant,
        to: Instant
    ): Iterable<TimeEntry>
}
package ru.worklog.repositories

import org.springframework.data.repository.CrudRepository
import ru.worklog.domain.DomainUser
import ru.worklog.domain.ExternalTask
import ru.worklog.domain.TimeEntry
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
    fun findTimeEntriesByUserAndStartTimestampBetweenOrderByStartTimestampDesc(
        user: DomainUser,
        from: Instant,
        to: Instant
    ): Iterable<TimeEntry>
}

interface ExternalTaskRepository : CrudRepository<ExternalTask, Long>
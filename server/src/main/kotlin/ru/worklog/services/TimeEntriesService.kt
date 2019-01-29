package ru.worklog.services

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.worklog.domain.TimeEntry
import ru.worklog.repositories.TimeEntriesRepository
import ru.worklog.shared.TimeEntryDto
import java.time.*

@Component
@Transactional
class TimeEntriesService(
    private val timeEntriesRepository: TimeEntriesRepository,
    private val userService: UserService,
    private val externalTaskService: ExternalTaskService
) {

    fun saveOrUpdate(timeEntryDto: TimeEntryDto): TimeEntry {

        val timeEntry = timeEntriesRepository.findById(timeEntryDto.entryID).orElseGet {
            val currentUser = SecurityContextHolder.getContext().authentication.name
            val user = userService.getUserByLogin(currentUser).get()
            TimeEntry(startTimestamp = Instant.parse(timeEntryDto.start), user = user)
        }
        Instant.now().toString()
        if (timeEntryDto.burnComment != null) timeEntry.burndownComment = timeEntryDto.burnComment!!
        if (timeEntryDto.shortDesc != null) timeEntry.shortDescription = timeEntryDto.shortDesc!!
        if (timeEntryDto.end != null) timeEntry.endTimestamp = Instant.parse(timeEntryDto.end)
        if (timeEntryDto.task != null) {
            val taskId = timeEntryDto.task!!.id
            val task = externalTaskService.getTask(taskId)
                .orElseThrow { IllegalArgumentException("invalid task id [$taskId]") }
            timeEntry.task = task
        }
        val save = timeEntriesRepository.save(timeEntry)
        return save
    }

    fun deleteTimeEntry(id: Long) {
        timeEntriesRepository.deleteById(id)
    }

    fun getEntriesByDate(date: LocalDate): Iterable<TimeEntry> {
        val midnight = date.atStartOfDay().toInstant(ZoneOffset.UTC)
        val endOfDay = date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)

        val currentUser = SecurityContextHolder.getContext().authentication.name
        val user = userService.getUserByLogin(currentUser).get()

        val timestampDesc =
            timeEntriesRepository.findTimeEntriesByUserAndStartTimestampBetweenOrderByStartTimestampDesc(
                user,
                midnight,
                endOfDay
            )
        return timestampDesc
    }
}
package ru.worklog.services

import ru.worklog.domain.TimeEntry
import ru.worklog.shared.TimeEntryDto

object Mapper {
    fun timeEntryToDto(timeEntry: TimeEntry): TimeEntryDto {
        return timeEntry.let {
            TimeEntryDto(
                it.startTimestamp.toString(),
                it.endTimestamp?.toString(),
                it.shortDescription,
                it.burndownComment,
                it.id
            )
        }
    }
}
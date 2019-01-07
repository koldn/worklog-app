package ru.tztservice.services

import ru.tztservice.domain.TimeEntry
import ru.tztservice.shared.TimeEntryDto

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
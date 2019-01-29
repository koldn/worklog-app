package ru.worklog.services

import ru.worklog.domain.DomainUser
import ru.worklog.domain.ExternalTask
import ru.worklog.domain.TimeEntry
import ru.worklog.shared.ExternalTaskDto
import ru.worklog.shared.TimeEntryDto
import ru.worklog.synchronizer.TaskDescriptor

object Mapper {
    fun timeEntryToDto(timeEntry: TimeEntry): TimeEntryDto {
        val task = if (timeEntry.task != null) taskToDto(timeEntry.task!!) else null
        return timeEntry.let {
            TimeEntryDto(
                it.startTimestamp.toString(),
                it.endTimestamp?.toString(),
                it.shortDescription,
                it.burndownComment,
                it.id,
                task
            )
        }
    }

    fun taskDescriptorToDto(taskDescriptor: TaskDescriptor): ExternalTaskDto {
        return ExternalTaskDto(externalId = taskDescriptor.externalID, title = taskDescriptor.title)
    }

    fun taskToDto(externalTask: ExternalTask): ExternalTaskDto {
        return ExternalTaskDto(
            externalId = externalTask.extID,
            title = externalTask.title,
            colorCode = externalTask.colorCode,
            id = externalTask.id
        )
    }
}
package ru.worklog.services

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.worklog.domain.ExternalTask
import ru.worklog.repositories.ExternalTaskRepository
import ru.worklog.shared.ExternalTaskDto
import ru.worklog.synchronizer.Synchronizer
import ru.worklog.synchronizer.TaskDescriptor
import java.util.*

@Component
@Transactional
class ExternalTaskService(
    private val sync: Synchronizer?,
    private val externalTaskRepository: ExternalTaskRepository,
    private val userService: UserService
) {

    fun searchTasks(searchString: String): Collection<TaskDescriptor> {
        if (sync != null) {
            return sync.searchTasks(searchString)
        }
        return emptyList()
    }

    @Transactional(readOnly = true)
    fun getTask(id: Long): Optional<ExternalTask> {
        return externalTaskRepository.findById(id)
    }

    fun save(externalTaskDto: ExternalTaskDto): ExternalTask {
        val externalTask = externalTaskRepository.findById(externalTaskDto.id).orElseGet {
            val currentUser = SecurityContextHolder.getContext().authentication.name
            val user = userService.getUserByLogin(currentUser).get()
            externalTaskDto.let {
                ExternalTask(extID = it.externalId, title = it.title, colorCode = it.colorCode, user = user)
            }
        }
        return externalTaskRepository.save(externalTask)
    }

    fun delete(taskID: Long) {
        externalTaskRepository.deleteById(taskID)
    }

}
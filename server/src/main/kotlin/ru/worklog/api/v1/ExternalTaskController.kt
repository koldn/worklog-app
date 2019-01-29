package ru.worklog.api.v1

import org.springframework.web.bind.annotation.*
import ru.worklog.services.ExternalTaskService
import ru.worklog.services.Mapper
import ru.worklog.shared.ExternalTaskDto

@RestController
@RequestMapping("/api/v1/tasks")
class ExternalTaskController(private val externalTaskService: ExternalTaskService) {

    @GetMapping("/search-ext/{searchString}")
    fun searchTasks(@PathVariable("searchString") searchString: String): List<ExternalTaskDto> {
        return externalTaskService.searchTasks(searchString).map { Mapper.taskDescriptorToDto(it) }
    }

    @PostMapping("/save")
    fun save(externalTaskDto: ExternalTaskDto): ExternalTaskDto {
        val task = externalTaskService.save(externalTaskDto)
        return Mapper.taskToDto(task)
    }

    @DeleteMapping("/delete")
    fun deleteTask(@RequestParam("taskID") taskID: Long) {
        externalTaskService.delete(taskID)
    }
}
package ru.worklog.api.v1

import org.springframework.web.bind.annotation.*
import ru.worklog.services.Mapper
import ru.worklog.services.TimeEntriesService
import ru.worklog.shared.TimeEntryDto
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/time_entries")
class TimeEntriesController(private val timeEntriesService: TimeEntriesService) {

    @PostMapping("/save")
    fun start(@RequestBody timeEntryDto: TimeEntryDto): TimeEntryDto {
        return Mapper.timeEntryToDto(timeEntriesService.saveOrUpdate(timeEntryDto))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestParam("entryID") entryID: Long) {
        timeEntriesService.deleteTimeEntry(entryID)
    }

    @GetMapping("/get/{date}")
    fun getEntriesByDate(@PathVariable("date") dateString: String): List<TimeEntryDto> {
        val date = LocalDate.parse(dateString)
        val entriesByDate = timeEntriesService.getEntriesByDate(date)
        return entriesByDate.map { Mapper.timeEntryToDto(it) }
    }
}
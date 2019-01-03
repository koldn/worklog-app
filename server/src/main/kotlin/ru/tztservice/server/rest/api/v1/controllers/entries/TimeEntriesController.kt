package ru.tztservice.server.rest.api.v1.controllers.entries

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/entries")
class TimeEntriesController() {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(TimeEntriesController::class.java)
    }

    @RequestMapping("/create")
    fun create(): String {
        return "I see this!";
    }


}
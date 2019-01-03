package ru.tztservice.server.rest.api.v1.controllers.entries

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TimeEntriesControllerTest {

    @Autowired
    lateinit var timeEntriesController: TimeEntriesController

    @Test
    internal fun `rest save`() {
        val mvc = MockMvcBuilders.standaloneSetup(timeEntriesController).build();
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/entries/create")).andExpect(MockMvcResultMatchers.status().isOk)
    }
}

package ru.tztservice.server

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [BasicIntegrationTest.MongoInit::class])
internal class BasicIntegrationTest {

    internal class MongoInit : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.data.mongodb.host=${mongoContainer.containerIpAddress}",
                "spring.data.mongodb.port=${mongoContainer.getMappedPort(27017)}"
            ).applyTo(applicationContext)
        }
    }

    private class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

    companion object {
        private val mongoContainer: KGenericContainer = KGenericContainer("mongo:4.0.4").withExposedPorts(27017)

        @JvmStatic
        @BeforeAll
        fun startContainer() {
            mongoContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun stopContainer() {
            mongoContainer.stop()
        }
    }
    @LocalServerPort
    var port : Int? = null
}

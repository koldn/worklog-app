package ru.tztservice

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
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [BasicIntegrationTest.MongoInit::class])
internal class BasicIntegrationTest {

    internal class MongoInit : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.data.mongodb.host=${postgresContainer.containerIpAddress}",
                "spring.data.mongodb.port=${postgresContainer.getMappedPort(27017)}"
            ).applyTo(applicationContext)
        }
    }

    private class KGenericContainer : PostgreSQLContainer<KGenericContainer>()

    companion object {
        private val postgresContainer: KGenericContainer = KGenericContainer()

        @JvmStatic
        @BeforeAll
        fun startContainer() {
            postgresContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun stopContainer() {
            postgresContainer.stop()
        }
    }
    @LocalServerPort
    var port : Int? = null
}

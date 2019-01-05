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
@ContextConfiguration(initializers = [BasicIntegrationTest.DBInit::class])
internal class BasicIntegrationTest {

    internal class DBInit : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.driver-class-name=org.postgresql.Driver",
                "spring.datasource.url=${postgresContainer.jdbcUrl}",
                "spring.datasource.username=${postgresContainer.username}",
                "spring.datasource.password=${postgresContainer.password}",
                "spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false",
                "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect",
                "spring.jpa.hibernate.ddl-auto=update"
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
    var port: Int? = null
}

package ru.tztservice

import org.hibernate.engine.spi.SessionFactoryImplementor
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.repository.CrudRepository
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.tztservice.repositories.DomainUserRepository
import ru.tztservice.repositories.TimeEntriesRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [BasicIntegrationTest.DBInit::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BasicIntegrationTest {

    @Autowired
    lateinit var allRepos: List<CrudRepository<*, *>>

    internal class DBInit : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                "spring.datasource.url=jdbc:tc:postgresql:9.6.8://hostname/databasename",
                "spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false",
                "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect",
                "spring.jpa.hibernate.ddl-auto=update"
            ).applyTo(applicationContext)
        }
    }

    @AfterAll
    fun clearDatabases() {
        allRepos.forEach { it.deleteAll() }
    }

    @LocalServerPort
    var port: Int? = null

}

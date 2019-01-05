package ru.tztservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class ServerApplication

fun main(args: Array<String>)
{
    runApplication<ServerApplication>(*args)
}

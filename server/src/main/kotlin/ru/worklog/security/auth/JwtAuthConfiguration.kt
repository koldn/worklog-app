package ru.worklog.security.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtAuthConfiguration {
    lateinit var secret: String
    lateinit var header: String
    lateinit var authPattern: String
    lateinit var tokenPrefix: String
    var expireInMins: Long? = 15
}
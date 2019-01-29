package ru.worklog.security.auth.filters

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import ru.worklog.security.auth.JwtAuthConfiguration
import ru.worklog.shared.CredentialsDto
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthFilter(val objectMapper: ObjectMapper, val jwtAuthConfiguration: JwtAuthConfiguration) :
    UsernamePasswordAuthenticationFilter() {

    init {
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher(jwtAuthConfiguration.authPattern, "POST"))
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val credentialsDto = objectMapper.readValue(request!!.inputStream, CredentialsDto::class.java)
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                credentialsDto.userName,
                credentialsDto.userPassword
            )
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        val expire = currentTimeMillis + TimeUnit.MINUTES.toMillis(jwtAuthConfiguration.expireInMins!!)
        val token = Jwts.builder().setSubject(authResult!!.name).setIssuedAt(Date(currentTimeMillis))
            .setExpiration(Date(expire)).signWith(SignatureAlgorithm.HS512, jwtAuthConfiguration.secret).compact()

        response!!.setHeader(jwtAuthConfiguration.header, token)
    }

}
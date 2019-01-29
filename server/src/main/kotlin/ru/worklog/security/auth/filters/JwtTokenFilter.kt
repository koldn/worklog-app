package ru.worklog.security.auth.filters

import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ru.worklog.security.auth.JwtAuthConfiguration
import ru.worklog.services.UserService
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(private val jwtAuthConfiguration: JwtAuthConfiguration, private val userService: UserService) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(jwtAuthConfiguration.header)
        if (null == token) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val claims = Jwts.parser().setSigningKey(jwtAuthConfiguration.secret).parseClaimsJws(token).body
            if (Date() > claims.expiration) {
                filterChain.doFilter(request, response)
                return
            }
            val userPresent = userService.isUserPresent(claims.subject)
            if (!userPresent) {
                filterChain.doFilter(request, response)
                return
            }
            val authenticationToken =
                UsernamePasswordAuthenticationToken(claims.subject, null, listOf())
            SecurityContextHolder.getContext().authentication = authenticationToken
            filterChain.doFilter(request, response)
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}
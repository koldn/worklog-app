package ru.tztservice.security.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.tztservice.security.auth.JwtAuthConfiguration
import ru.tztservice.security.auth.filters.JwtAuthFilter
import ru.tztservice.security.auth.filters.JwtTokenFilter
import ru.tztservice.security.services.DomainUserDetailsService
import ru.tztservice.services.UserService

@EnableWebSecurity
class SecurityConfigurer(
    val userDetailsService: DomainUserDetailsService,
    val objectMapper: ObjectMapper,
    val passwordEncoder: PasswordEncoder,
    val jwtAuthConfiguration: JwtAuthConfiguration,
    val userService: UserService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and().authorizeRequests().antMatchers(HttpMethod.POST, "/user/register/**").permitAll()
            .and().addFilterBefore(createAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests().antMatchers(HttpMethod.POST, jwtAuthConfiguration.authPattern).permitAll()
            .and().addFilterBefore(createTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests().anyRequest().fullyAuthenticated()
    }

    fun createAuthenticationFilter(): UsernamePasswordAuthenticationFilter {
        val jwtAuthFilter = JwtAuthFilter(objectMapper, jwtAuthConfiguration)
        jwtAuthFilter.setAuthenticationManager(authenticationManager())
        return jwtAuthFilter
    }

    fun createTokenFilter(): JwtTokenFilter {
        return JwtTokenFilter(jwtAuthConfiguration, userService)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }
}
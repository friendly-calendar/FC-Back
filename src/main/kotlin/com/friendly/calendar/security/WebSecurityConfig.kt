package com.friendly.calendar.security

import com.friendly.calendar.enums.ADMIN_ROLE
import lombok.RequiredArgsConstructor
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
class WebSecurityConfig(private val jwtProvider: JwtProvider) {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it
                .ignoring()
                .requestMatchers("/h2-console/**")
        }
    }

    @Bean
    @Profile("prod")
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .cors {}
            .csrf {}
            .sessionManagement {
                it
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterAt(
                JwtAuthenticationFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return httpSecurity.build()
    }

    @Bean
    @Profile("dev")
    fun devFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .cors { it.disable() }
            .csrf { it.disable() }
            .sessionManagement {
                it
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterAt(
                JwtAuthenticationFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java
            ).authorizeHttpRequests {
                it.requestMatchers(*PERMIT_ALL_URLS).permitAll()

                it.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN_ROLE)

                it.requestMatchers("/api/v1/**").authenticated()
            }

        return httpSecurity.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    companion object {
        val PERMIT_ALL_URLS = arrayOf(
            "/api/v1/auth",
            "/api/v1/users",
            "/api/v1/auth/refresh",
        )
    }
}

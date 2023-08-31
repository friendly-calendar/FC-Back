package com.friendly.calendar.config

import com.friendly.calendar.security.CalendarAuthFailHandler
import com.friendly.calendar.security.CalendarAuthSuccessHandler
import com.friendly.calendar.security.SignInAuthenticationFilter
import com.friendly.calendar.security.jwt.JwtAuthenticationFilter
import com.friendly.calendar.security.jwt.JwtAuthenticationProvider
import com.friendly.calendar.security.jwt.JwtTokenManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableWebMvc
class SecurityConfig(val calendarUserDetailsService : UserDetailsService) {

    private val ALLOW_URL = arrayOf("/api/user","/api/user/signIn", "/api/token/refresh","/alive")
    @Bean
    fun authenticationManager(http : HttpSecurity , jwtTokenManager: JwtTokenManager): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder
                .authenticationProvider(JwtAuthenticationProvider(calendarUserDetailsService,jwtTokenManager))
                .userDetailsService(calendarUserDetailsService)

        return authenticationManagerBuilder.build()
    }

    @Bean
    fun filterChain(http : HttpSecurity,
                    jwtTokenManager: JwtTokenManager,
                    authenticationManager: AuthenticationManager): SecurityFilterChain {
        val filter =  SignInAuthenticationFilter(authenticationManager)
        filter.setAuthenticationSuccessHandler(CalendarAuthSuccessHandler())
        filter.setAuthenticationFailureHandler(CalendarAuthFailHandler())
        return http
             // 회원가입  , 로그인 요청은 인증 불필요
             .authorizeHttpRequests().antMatchers(*ALLOW_URL).permitAll()
             // 나머지는 인증필요
             .anyRequest().authenticated()
             .and()
             .authenticationManager(authenticationManager(http, jwtTokenManager))
             .csrf().disable()
             .addFilterAfter(JwtAuthenticationFilter(jwtTokenManager,ALLOW_URL), LogoutFilter::class.java)
             .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
             .build()
    }

    @Profile("dev")
    @Bean
    fun devCorsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
                registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/")
            }

            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
            }
        }
    }

    @Profile("prod")
    @Bean
    fun prodCorsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
                registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/")
            }
        }
    }
}

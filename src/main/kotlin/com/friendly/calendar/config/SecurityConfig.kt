package com.friendly.calendar.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(val userDetailsService : UserDetailsService) {

//    @Bean
//    fun authManager(http : HttpSecurity): AuthenticationManager {
//        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
//        authenticationManagerBuilder.
//                userDetailsService(userDetailsService)
//        return authenticationManagerBuilder.build()
//    }

    @Bean
    fun filterChain(http : HttpSecurity): SecurityFilterChain {
         return http
//             // 회원가입  , 로그인 요청은 인증 불필요
//             .authorizeHttpRequests().antMatchers("/signUp","/login").permitAll()
//             // 나머지는 인증필요
//             .anyRequest().authenticated().and()
//             .authenticationManager(authManager(http))
             // 로그인 url 설정
             .formLogin()
                 .loginPage("/login")
                 .permitAll().and()
             .build()
    }


}
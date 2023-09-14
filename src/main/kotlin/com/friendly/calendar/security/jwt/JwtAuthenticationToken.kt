package com.friendly.calendar.security.jwt

import com.friendly.calendar.network.TokenResponse
import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthenticationToken(val id: String, val password: String, @JvmField val token: TokenResponse? = null) : AbstractAuthenticationToken(listOf()) {

    override fun getPrincipal(): String = id

    override fun getCredentials(): String = password

    fun getToken(): TokenResponse? = token
}

package com.friendly.calendar.network

import java.io.Serializable

data class UserSignInReq(
    val id: String? = null,
    val password: String? = null
) : Serializable

package com.friendly.calendar.network.user

data class UserSignUpReq(

    val nickName: String?,

    val email: String?,

    val id: String,

    val password: String,

    val phoneNumber: String
)
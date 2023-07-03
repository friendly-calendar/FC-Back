package com.friendly.calendar.network.user

import com.friendly.calendar.validator.annotation.AlphaNumericWithSpecialChars
import com.friendly.calendar.validator.annotation.Phone
import com.friendly.calendar.validator.annotation.AlphaNumeric
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class UserSignUpReq(

    @field:Size(min = 2, max = 10, message = "nickName 의 Size 는 {min} ~ {max} 사이입니다.")
    @AlphaNumeric
    val nickName: String?,

    @field:Email
    val email: String?,

    @field:Size(min = 4, max = 15, message = "id의 Size 는 {min} ~ {max} 사이입니다.")
    @AlphaNumericWithSpecialChars
    val id: String,

    @field:Size(min = 9, max = 20, message = "password 의 Size 는 {min} ~ {max} 사이입니다.")
    @AlphaNumericWithSpecialChars
    val password: String,

    @Phone
    val phoneNumber: String
)
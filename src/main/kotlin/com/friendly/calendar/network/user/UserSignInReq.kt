package com.friendly.calendar.network.user

import lombok.NoArgsConstructor
import java.io.Serializable

class UserSignInReq : Serializable {

    var id: String? = null

    var password: String? = null

    constructor()

    constructor(id :String, password :String){
        this.id = id ;
        this.password = password;
    }

}
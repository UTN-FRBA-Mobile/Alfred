package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.User

open class Permissions(val user: User) {

    open fun getUserId() : String {
        // por ahora, permisos hardcodeados
        return "pjvgEdPAyRabtAdhk"
    }

}
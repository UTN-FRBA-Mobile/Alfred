package com.botigocontigo.alfred

import com.botigocontigo.alfred.backend.Permissions

class User() {

    fun getNickname(): String {
        // por ahora, nombre hardcodeado
        return "Max Power"
    }

    fun getPermissions(): Permissions {
        // por ahora, permisos hardcodeados
        return Permissions("pjvgEdPAyRabtAdhk")
    }
}
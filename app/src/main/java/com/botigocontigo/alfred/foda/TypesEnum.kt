package com.botigocontigo.alfred.foda

import java.util.stream.Stream

enum class TypesEnum(val type: String) {
    AMENAZAS("threats"),
    DEBILIDADES("weaknesses"),
    FORTALEZAS("strengths"),
    OPORTUNIDADES("opportunities"),
    THREATS("AMENAZAS"),
    WEAKNESSES("DEBILIDADES"),
    STRENGTHS("FORTALEZAS"),
    OPPORTUNITIES("OPORTUNIDADES");

    companion object {
        fun getType(type: String) = valueOf(type.toUpperCase())
    }

}
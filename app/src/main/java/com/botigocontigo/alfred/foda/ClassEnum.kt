package com.botigocontigo.alfred.foda

enum class ClassEnum(val clase: String) {
    THREATS("EXTERNO"),
    WEAKNESSES("INTERNO"),
    STRENGTHS("INTERNO"),
    OPPORTUNITIES("EXTERNO");

    companion object {
        fun getClass(clase: String) = ClassEnum.valueOf(clase.toUpperCase())
    }
}
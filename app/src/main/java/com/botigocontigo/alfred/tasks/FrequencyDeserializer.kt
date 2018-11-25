package com.botigocontigo.alfred.tasks

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class FrequencyDeserializer : JsonDeserializer<Frequency>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Frequency {
        val jsonObject = json!!.asJsonObject

        return Frequency(
                jsonObject.get("type").asString,
                jsonObject.get("value").asString
        )
    }

}
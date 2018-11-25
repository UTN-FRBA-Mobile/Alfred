package com.botigocontigo.alfred.tasks

import com.botigocontigo.alfred.foda.Dimension
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class FodaDeserealizer : JsonDeserializer<Dimension> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Dimension {
        val jsonObject = json.asJsonObject


        return Dimension(
                jsonObject.get("id").asInt,
                jsonObject.get("name").asString,
                jsonObject.get("userId").asString,
                arrayOf(jsonObject.get("descriptions").asString)
        )
    }

}

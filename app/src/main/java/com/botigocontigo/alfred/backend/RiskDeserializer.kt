package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.storage.db.entities.Area
import com.botigocontigo.alfred.storage.db.entities.Risk
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class RiskDeserializer: JsonDeserializer<Risk> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Risk {
        val jsonObject = json.asJsonObject
        return Risk(
                jsonObject.get("_id").asInt,
                jsonObject.get("risk").asString,
                jsonObject.get("probability").asString,
                jsonObject.get("impact").asString,
                jsonObject.get("detectionCapacity").asString,
                jsonObject.get("userId").asString
        )
    }

}

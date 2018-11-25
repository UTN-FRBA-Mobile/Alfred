package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.storage.db.entities.Area
import com.botigocontigo.alfred.tasks.Plan
import com.botigocontigo.alfred.tasks.Task
import com.botigocontigo.alfred.tasks.TaskDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class AreasDeserializer  : JsonDeserializer<Area> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Area {
        val jsonObject = json.asJsonObject
        return Area(
                jsonObject.get("_id").asInt,
                jsonObject.get("userId").asString,
                jsonObject.get("name").asString,
                jsonObject.get("segments").asString,
                jsonObject.get("relationships").asString,
                jsonObject.get("channels").asString,
                jsonObject.get("valueProposition").asString,
                jsonObject.get("activities").asString,
                jsonObject.get("resources").asString,
                jsonObject.get("partners").asString,
                jsonObject.get("income").asString,
                jsonObject.get("costs").asString
        )
    }

}
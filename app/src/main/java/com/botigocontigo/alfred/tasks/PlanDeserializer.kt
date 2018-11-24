package com.botigocontigo.alfred.tasks

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.storage.db.entities.Plan
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class PlanDeserializer  : JsonDeserializer<Plan> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Plan {
        val jsonObject = json.asJsonObject

        val formatter = SimpleDateFormat("M/d/yy hh:mm a")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormated: Date = formatter.parse(jsonObject.get("createdAt").asString)

        return Plan(
                jsonObject.get("_id").asInt,
                jsonObject.get("name").asString,
                jsonObject.get("businessArea").asString,
                jsonObject.get("userId").asString,
                jsonObject.get("userEmail").asString,
                dateFormated
                //FIXME serialize all plans when Erik adds the field
        )
    }

}

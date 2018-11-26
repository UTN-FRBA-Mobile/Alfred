package com.botigocontigo.alfred.tasks

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.ArticleDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class PlanDeserializer  : JsonDeserializer<Plan> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Plan {
        val jsonObject = json.asJsonObject

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val strDate = jsonObject.get("createdAt").asString.substring(0, 10)
        val dateFormated: Date = formatter.parse(strDate)

        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Task::class.java, TaskDeserializer())
        val gson = gsonBuilder.create()

        val tasksDeserialized : List<Task> = gson.fromJson(jsonObject.get("tasks").asString , Array<Task>::class.java).toList()

        return Plan(
                jsonObject.get("_id").asString,
                jsonObject.get("name").asString,
                jsonObject.get("businessArea").asString,
                jsonObject.get("userId").asString,
                jsonObject.get("userEmail").asString,
                dateFormated,
                tasksDeserialized
        )
    }

}

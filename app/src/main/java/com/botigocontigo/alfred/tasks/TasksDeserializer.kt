package com.botigocontigo.alfred.tasks

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class TaskDeserializer : JsonDeserializer<Task> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Task {
        val jsonObject = json.asJsonObject

        val json = jsonObject.get("frequency").asString
        val gson = Gson()
        val frequencyTransformed : Frequency = gson.fromJson(json, Frequency::class.java)

        return Task(
                jsonObject.get("taskDescription").asString,
                jsonObject.get("responsibleID").asString,
                jsonObject.get("supervisorID").asString,
                frequencyTransformed,
                jsonObject.get("completed").asBoolean

        )
    }

}
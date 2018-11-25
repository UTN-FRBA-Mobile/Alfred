package com.botigocontigo.alfred.tasks

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class TaskDeserializer: JsonDeserializer<Task> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Task {
        val jsonObject = json!!.asJsonObject

        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Task::class.java, FrequencyDeserializer())
        val gson = gsonBuilder.create()

        val frequencyDeserialized : Frequency = gson.fromJson(jsonObject.get("frequency").asString , Frequency::class.java)

        return Task(
                jsonObject.get("_id").asString,
                jsonObject.get("taskDescription").asString,
                jsonObject.get("responsibleID").asString,
                jsonObject.get("supervisorID").asString,
                frequencyDeserialized,
                jsonObject.get("status").asString,
                jsonObject.get("completed").asBoolean
        )
    }

}
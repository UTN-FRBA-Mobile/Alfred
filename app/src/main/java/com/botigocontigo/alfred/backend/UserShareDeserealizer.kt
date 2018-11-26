package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.UserShare
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserShareDeserealizer : JsonDeserializer<UserShare> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserShare {
        val jsonObject = json.asJsonObject

        return UserShare(
                jsonObject.get("userId").asString,
                jsonObject.get("email").asString
        )
    }

}

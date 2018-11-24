package com.botigocontigo.alfred.learn

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ArticleDeserializer : JsonDeserializer<Article> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Article {
        val jsonObject = json.asJsonObject

        return Article(
                jsonObject.get("title").asString,
                jsonObject.get("description").asString,
                jsonObject.get("link").asString,
                jsonObject.get("imageUrl").asString

        )
    }

}

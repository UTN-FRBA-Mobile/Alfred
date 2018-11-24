package com.botigocontigo.alfred.storage.db.converters

import android.arch.persistence.room.TypeConverter
import com.botigocontigo.alfred.storage.db.entities.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class TaskConverter {
    @TypeConverter
    fun toTaskList (value: String?): List<Task> {
        if (value == null) {
            return listOf<Task>();
        }
        val listType = object : TypeToken<List<Task>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromTaskList (value: List<Task>): String {
        return Gson().toJson(value)
    }
}
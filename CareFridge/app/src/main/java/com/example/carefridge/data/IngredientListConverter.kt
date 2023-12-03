package com.example.carefridge.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IngredientListConverter {
    @TypeConverter
    fun listToJson(value: List<Pair<String, Int>>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String?): List<Pair<String, Int>> {
        val listType = object : TypeToken<List<Pair<String, Int>>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
package com.example.carefridge.data

import androidx.room.TypeConverter
import com.example.carefridge.data.entities.Ingredient
import com.google.gson.Gson

class IngredientListConverter {
    @TypeConverter
    fun listToJson(value: List<Ingredient>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String?) = Gson().fromJson(value, Array<Ingredient?>::class.java).toList()
}
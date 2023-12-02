package com.example.carefridge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeTable")
data class Recipe(
    val name: String = "",
    val ingredientList: List<Ingredient>
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

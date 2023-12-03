package com.example.carefridge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeTable")
data class Recipe(
    val name: String = "",
    val ingredients: List<Pair<String, Int>>,
//    val ingredientList: List<Ingredient>,
//    val map: Map<String, Int>
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

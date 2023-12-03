package com.example.carefridge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeTable")
data class Recipe(
    val name: String = "",  // 레시피 이름
    val ingredients: List<Pair<String, Int>>, // 레시피 재료<재료 이름, 필요한 양>
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

package com.example.carefridge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeTable")
data class Recipe(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String = "",  // 레시피 이름
    val ingredients: List<Pair<String, Int>>, // 레시피 재료<재료 이름, 필요한 양>
    val isRecommendTarget: Boolean = true   // 추천 대상에 넣어줄지를 저장
)

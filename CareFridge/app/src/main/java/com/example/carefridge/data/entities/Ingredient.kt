package com.example.carefridge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "IngredientTable")
data class Ingredient(
   val name: String = "",
   val amount: Int = 50,
   val daysToExpiration: Long = System.currentTimeMillis(),
   val isPrefer: Boolean = false
) {
   @PrimaryKey(autoGenerate = true) var id: Int = 0
}
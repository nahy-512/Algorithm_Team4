package com.example.carefridge.data.entities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(tableName = "IngredientTable")
data class Ingredient(
   val name: String = "",
   val amount: Int = 50,
   val daysToExpiration: Long = System.currentTimeMillis(),
   val isPrefer: Boolean = false
) {
   @PrimaryKey(autoGenerate = true) var id: Int = 0

   // 함수를 통해 daysToExpiration을 원하는 형식으로 변환
   @RequiresApi(Build.VERSION_CODES.O)
   fun getFormattedExpirationDate(): String {
      val instant = Instant.ofEpochMilli(daysToExpiration)
      val expirationDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      return expirationDate.format(formatter)
   }
}
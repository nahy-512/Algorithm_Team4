package com.example.carefridge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(tableName = "IngredientTable")
data class Ingredient(
   val name: String = "",
   val amount: Int = 50,
   var expirationDate: Long = System.currentTimeMillis(),
   val isPrefer: Boolean = false
) {
   @PrimaryKey(autoGenerate = true) var id: Int = 0

   // 함수를 통해 daysToExpiration을 원하는 형식으로 변환
   fun getFormattedExpirationDate(): String {
      val instant = Instant.ofEpochMilli(expirationDate)
      val expirationDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      return expirationDate.format(formatter)
   }

   fun getDaysToExpiration(expirationDate: Long): Int {
      val koreaZoneId = ZoneId.of("Asia/Seoul")
      // 현재 시간
      val currentDateTime = LocalDateTime.now(koreaZoneId)
      // 유통기한 시간
      val expirationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(expirationDate), koreaZoneId)
      // 날짜 기준으로 디데이 계산
      val duration = Duration.between(currentDateTime, expirationDateTime)
      // 일(day)로 변환
      val dDay = duration.toDays().toInt()

      return dDay
   }
}
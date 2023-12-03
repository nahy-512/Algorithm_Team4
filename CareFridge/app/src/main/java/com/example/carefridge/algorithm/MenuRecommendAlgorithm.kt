package com.example.carefridge.algorithm

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.data.entities.Recipe
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Scanner

// 메뉴 추천 알고리즘 테스트
object MenuRecommendAlgorithm {

    private var userPreferences // 사용자의 선호 재료 목록을 저장할 Set
            : MutableSet<String>? = null


    @RequiresApi(Build.VERSION_CODES.O)
    fun main() {
        initializeUserPreferences() // 사용자의 선호 재료 목록 초기화
        // 음식 재료 초기화
        val ingredients = arrayOf(
            Ingredient("고기", 500, getExpirationDateAfterDays(5)),  //500g 양과 유통기한 5일
            Ingredient("채소", 400, getExpirationDateAfterDays(3)),
            Ingredient("소스", 300, getExpirationDateAfterDays(10)),
            Ingredient("밥", 500, getExpirationDateAfterDays(11)),
            Ingredient("면", 500, getExpirationDateAfterDays(10)),
            Ingredient("빵", 400, getExpirationDateAfterDays(2)),
            Ingredient("소고기", 200, getExpirationDateAfterDays(3))
        )

        // 음식 레시피 초기화
        val recipes = arrayOf(
            Recipe("비빔밥", listOf("밥" to 100, "고기" to 100, "채소" to 100, "소스" to 50)), // 밥, 고기, 채소, 소스
            Recipe("짜장면", listOf("면" to 150, "고기" to 50, "채소" to 50, "소스" to 30)), // 면, 고기, 채소, 소스
            Recipe("스테이크", listOf("소고기" to 200, "채소" to 50, "소스" to 30)), // 소고기, 채소, 소스
            Recipe("스파게티", listOf("면" to 150, "소스" to 80)), // 면, 소스
            Recipe("샌드위치", listOf("빵" to 100, "고기" to 50, "채소" to 80)) // 빵, 고기, 채소
        )

//        val recipes = arrayOf(
//            Recipe("비빔밥", arrayOf("밥", "고기", "채소", "소스")), // 밥, 고기, 채소, 소스
//            Recipe("짜장면", arrayOf("면", "고기", "채소", "소스")), // 면, 고기, 채소, 소스
//            Recipe("스테이크", arrayOf("소고기", "채소", "소스")), // 소고기, 채소, 소스
//            Recipe("스파게티", arrayOf("면", "소스")), // 면, 소스
//            Recipe("샌드위치", arrayOf("빵", "고기", "채소")) // 빵, 고기, 채소
//        )

        // 사용자에게 선호하는 재료 입력 받기
        userPreferences!!.add("고기")
//        val scanner = Scanner(System.`in`)
//        println("선호하는 재료를 입력하세요. (예: 고기, 면)")
//        println("입력을 마치려면 엔터키")
//        while (true) {
//            val input = scanner.nextLine()
//            if (input.isEmpty()) {
//                break
//            }
//            userPreferences!!.add(input)
//        }

        // 메뉴 추천 및 결과 출력
        Log.d("RecommendTest", "추천 음식: " + recommendMenu(ingredients, recipes))
    }

    // 사용자의 선호 재료 목록 초기화
    private fun initializeUserPreferences() {
        userPreferences = HashSet()
    }

    // 메뉴 추천 함수
    private fun recommendMenu(ingredients: Array<Ingredient>, recipes: Array<Recipe>): String {
        var recommendedMenu = ""
        var bestScore = -1.0

        // 각 레시피에 대해 점수를 계산하고 최고 점수를 가진 음식을 추천
        for (recipe in recipes) {
            var score = 0.0

            // 현재 레시피에 필요한 각 재료에 대해 점수를 계산하고 더함
            for (data in recipe.ingredients) {
                val ingredientName = data.first
                if (userPreferences!!.contains(ingredientName)) {
                    // 사용자가 선호하는 재료에 해당하는 경우 가중치 부여
                    for (ingredient in ingredients) {
                        if (ingredient.name == ingredientName) {
                            // 가중치 부여
                            score += calculateWeight(ingredient)
                        }
                    }
                }
            }

            // 사용자가 선호하는 재료가 있다면, 사용된 레시피에만 가중치를 부여 / 또는 아예 없을 때
            if (score > 0 || userPreferences!!.isEmpty()) {
                // 사용자가 선호하는 재료가 사용된 경우에만 최종 점수를 계산
                score /= recipe.ingredients.size // 재료 수로 정규화

                // 최고 점수보다 현재 레시피의 점수가 높으면 갱신
                if (score > bestScore) {
                    bestScore = score
                    recommendedMenu = recipe.name
                }
            }
        }
        return recommendedMenu
    }

    // 양과 유통기한을 고려한 가중치 계산 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateWeight(ingredient: Ingredient): Double {
        val amountWeight = ingredient.amount / 100.0 // 양이 많을수록 긍정적 가중치
        val expirationWeight = (100 - ingredient.getDaysToExpiration(ingredient.expirationDate)) / 100.0 // 유통기한이 짧을수록 긍정적 가중치

        // 양과 유통기한을 종합한 최종 가중치
        return amountWeight * expirationWeight
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExpirationDateAfterDays(daysToAdd: Long): Long {
        val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val expirationDateTime = currentDateTime.plusDays(daysToAdd)
        // 현재 날짜를 기준으로 인자로 들어온 daysToAdd를 더한 유통기한을 반환
        return expirationDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
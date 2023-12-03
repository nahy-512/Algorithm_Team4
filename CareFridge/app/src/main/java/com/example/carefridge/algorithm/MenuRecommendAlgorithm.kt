package com.example.carefridge.algorithm

import android.util.Log
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.data.entities.Recipe

// 메뉴 추천 알고리즘 코드
object MenuRecommendAlgorithm {

    // 사용자의 선호 재료 목록을 저장할 Set
    private var userPreferences : List<String>? = null

    fun main(ingredients: List<Ingredient>, recipes: List<Recipe>, userPreferences: List<String>): String {
        initializeUserPreferences(userPreferences) // 사용자의 선호 재료 목록 초기화

        Log.d("RecommendAlgo", "재료: ${ingredients},\n레시피: ${recipes},\n사용자 선호 음식: $userPreferences")

        val recorecommendMenu = recommendMenu(ingredients, recipes)

        // 메뉴 추천 및 결과 출력
        Log.d("RecommendAlgo", "추천 음식: $recorecommendMenu")

        return recorecommendMenu
    }

    // 사용자의 선호 재료 목록 초기화
    private fun initializeUserPreferences(userPreferences: List<String>) {
        this.userPreferences = userPreferences
    }

    // 메뉴 추천 함수
    private fun recommendMenu(ingredients: List<Ingredient>, recipes: List<Recipe>): String {
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
    private fun calculateWeight(ingredient: Ingredient): Double {
        val amountWeight = ingredient.amount / 100.0 // 양이 많을수록 긍정적 가중치
        val expirationWeight = (100 - ingredient.getDaysToExpiration(ingredient.expirationDate)) / 100.0 // 유통기한이 짧을수록 긍정적 가중치

        // 양과 유통기한을 종합한 최종 가중치
        return amountWeight * expirationWeight
    }
}
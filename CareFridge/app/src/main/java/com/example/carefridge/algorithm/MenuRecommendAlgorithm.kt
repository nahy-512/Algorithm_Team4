package com.example.carefridge.algorithm

import android.util.Log
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.data.entities.Recipe

// 냉장고 음식 기반 메뉴 추천 알고리즘 코드
object MenuRecommendAlgorithm {

    // 사용자의 선호 재료 목록을 저장할 Set
    private var userPreferences : List<String>? = null

    fun main(ingredients: List<Ingredient>, recipes: List<Recipe>, userPreferences: List<String>): String {
        // 사용자의 선호 재료 목록 초기화
        initializeUserPreferences(userPreferences)
        // 인자로 잘 넘어왔는지 로그 확인
        Log.d("RecommendAlgo", "재료: ${ingredients},\n레시피: ${recipes},\n사용자 선호 음식: $userPreferences")

        // 추천 받은 메뉴를 반환
        return recommendMenu(ingredients, recipes)
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
                for (ingredient in ingredients) {
                    // 양과 유통기한을 고려해서 가중치 계산 진행
                    if (ingredient.name == ingredientName) {
                        // 가중치 계산 결과를 score에 저장
                        score += calculateWeight(ingredient)
                    }
                    /// 사용자가 선호하는 재료가 들어간 레시피에는 가중치를 추가 부여
                    if (userPreferences!!.contains(ingredientName)) {
                        // 추가 가중치 부여
                        score += 100
                    }
                }
            }

            // 최종적으로 현재의 레시피에서 가중치가 가장 높은 메뉴 저장
            if (score > 0) {
                score /= recipe.ingredients.size // score를 재료 수로 정규화

                // 현재 저장된 최고 점수보다 높다면 추천 메뉴 업데이트
                if (score > bestScore) {
                    bestScore = score
                    recommendedMenu = recipe.name
                }
            }
        }
        // 메뉴 추천 결과 출력
        Log.d("RecommendAlgo", "추천 음식: $recommendedMenu")

        // 추천 알고리즘을 통해 얻은 추천 메뉴를 반환
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
package com.example.carefridge.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.carefridge.data.entities.Recipe

@Dao
interface RecipeDao {
    @Insert
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)

    @Query("SELECT * FROM RecipeTable")
    fun getRecipes(): List<Recipe>

    @Query("SELECT * FROM RecipeTable")
    fun getRecipesLiveData(): LiveData<List<Recipe>>

    // 추천할 수 있는 레시피 목록 조회하기
    @Query("SELECT * FROM RecipeTable WHERE isRecommendTarget = :canRecommend")
    fun getRecommendTargetRecipes(canRecommend: Boolean = true): List<Recipe>

    // 레시피의 추천 여부를 수정 (레시피 재추천 기능을 위함)
    @Query("UPDATE RecipeTable SET isRecommendTarget = :canRecommend WHERE id = :id")
    fun updateIsRecommendTargetById(id: Int, canRecommend: Boolean)

    @Query("UPDATE RecipeTable SET isRecommendTarget = 1 WHERE isRecommendTarget = 0")
    fun resetRecommendStatus()

    // id로 레시피 찾기
    @Query("SELECT * FROM RecipeTable WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Recipe?
}
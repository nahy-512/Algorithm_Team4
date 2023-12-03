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

    // id로 레시피 찾기
    @Query("SELECT * FROM RecipeTable WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Recipe?
}
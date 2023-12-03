package com.example.carefridge.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.carefridge.data.entities.Ingredient

@Dao
interface IngredientDao {
    @Insert
    fun insert(ingredient: Ingredient)

    @Update
    fun update(ingredient: Ingredient)

    @Delete
    fun delete(ingredient: Ingredient)

    @Query("SELECT * FROM ingredientTable")
    fun getIngredients(): List<Ingredient>

    @Query("SELECT * FROM ingredientTable")
    fun getIngredientsLiveData(): LiveData<List<Ingredient>>

    // 사용자가 선호하는 재료 목록을 가져옴
    @Query("SELECT * FROM ingredienttable WHERE isPrefer = :isPrefer")
    fun getUserPreferIngredients(isPrefer: Boolean): List<Ingredient>
}

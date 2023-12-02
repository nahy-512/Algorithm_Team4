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
}

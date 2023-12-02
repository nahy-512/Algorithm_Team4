package com.example.carefridge.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.data.entities.Recipe
import com.example.carefridge.data.dao.IngredientDao
import com.example.carefridge.data.dao.RecipeDao

@Database(entities = [Ingredient::class, Recipe::class], version = 1)
@TypeConverters(IngredientListConverter::class)
abstract class FridgeDatabase: RoomDatabase() {

    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        private var instance: FridgeDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FridgeDatabase? {
            if (instance == null) {
                synchronized(FridgeDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FridgeDatabase::class.java,
                        "fridge-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}
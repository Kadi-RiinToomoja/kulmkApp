package com.example.kulmkapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KulmkappDao {

    // Ingredients

    @Query("SELECT * FROM Ingredients")
    fun loadAllIngredients(): List<IngredientEntity>

    @Query("SELECT * FROM Ingredients WHERE id==:ingredientId")
    fun loadSingleIngredientById(ingredientId: Int): IngredientEntity

    @Query("SELECT * FROM Ingredients WHERE name like :ingredientName || '%'")
    fun loadIngredientsLikeName(ingredientName: String): List<IngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(vararg ingredient: IngredientEntity)

    // KulmkappItems

    @Query("SELECT * FROM KulmkappItems")
    fun loadAllKulmkappItems(): List<IngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKulmkappItem(vararg kulmkappItem: KulmkappItemEntity)

    // Recipes

    @Query("SELECT * FROM Recipes")
    fun loadAllRecipes(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(vararg recipe: RecipeEntity)



}
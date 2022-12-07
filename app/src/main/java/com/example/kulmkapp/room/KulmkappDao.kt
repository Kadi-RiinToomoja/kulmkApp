package com.example.kulmkapp.room

import androidx.room.*

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

    @Query("DELETE FROM Ingredients WHERE id==:ingredientId")
    fun deleteIngredient(ingredientId: Int)

    // KulmkappItems

    @Query("SELECT * FROM KulmkappItems")
    fun loadAllKulmkappItems(): List<KulmkappItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKulmkappItem(vararg kulmkappItem: KulmkappItemEntity)

    @Query("DELETE FROM KulmkappItems WHERE id==:id")
    fun deleteKulmkappItem(id: Int)

    // Recipes

    @Query("SELECT * FROM Recipes")
    fun loadAllRecipes(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(vararg recipe: RecipeEntity)

    @Query("UPDATE Recipes SET recipeUrl = :recipeUrl WHERE id = :id")
    fun updateRecipe(id: Int, recipeUrl: String)

    @Query("SELECT * FROM RecipeIngredients WHERE recipeId == :id")
    fun loadAllIngredientsForRecipe(vararg id: Int): List<RecipeIngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeIngredient(vararg recipe: RecipeIngredientEntity)

    @Query("DELETE FROM Recipes WHERE id==:id")
    fun deleteRecipe(id: Int)

    @Query("DELETE FROM RecipeIngredients WHERE recipeId==:id")
    fun deleteRecipeIngredients(id: Int)


    @Transaction
    fun deleteRecipeAndItsIngredients(id : Int) {
        deleteRecipe(id)
        deleteRecipeIngredients(id)
    }
}
package com.example.kulmkapp.logic.room

import androidx.room.*

@Dao
interface FridgeDao {

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

    // FridgeItems

    @Query("SELECT * FROM FridgeItems WHERE isInFridge==1")
    fun loadAllFridgeItems(): List<FridgeItemEntity>

    @Query("SELECT * FROM FridgeItems WHERE isInFridge==0")
    fun loadAllShoppingListItems(): List<FridgeItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFridgeOrShoppingListItem(vararg kulmkappItem: FridgeItemEntity)

    @Query("DELETE FROM FridgeItems WHERE id==:id")
    fun deleteFridgeOrShoppingListItem(id: Int)

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
package com.example.kulmkapp.logic.room

import android.util.Log
import androidx.room.*

@Dao
interface FridgeDao {

    // Ingredients


    @Query("SELECT * FROM Ingredients")
    fun getAllIngredients(): List<IngredientEntity>

    @Query("SELECT * FROM Ingredients WHERE id==:ingredientId")
    fun getSingleIngredientById(ingredientId: Int): IngredientEntity

    @Query("SELECT * FROM Ingredients WHERE name like :ingredientName || '%'")
    fun getIngredientsLikeName(ingredientName: String): List<IngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(vararg ingredient: IngredientEntity)

    @Query("DELETE FROM Ingredients WHERE id==:ingredientId")
    fun deleteIngredient(ingredientId: Int)

    // FridgeItems

    @Query("SELECT * FROM FridgeItems WHERE isInFridge==1")
    fun getAllFridgeItems(): List<FridgeItemEntity>

    @Query("SELECT * FROM FridgeItems WHERE isInFridge==1 AND expireDate LIKE :date")
    fun getFridgeItemsByDate(date: String): List<FridgeItemEntity>

    @Query("SELECT * FROM FridgeItems WHERE id==:itemId")
    fun getSingleFridgeItemById(itemId: Int): FridgeItemEntity

    @Query("SELECT * FROM FridgeItems WHERE isInFridge==0")
    fun getAllShoppingListItems(): List<FridgeItemEntity>

    @Query("UPDATE FridgeItems SET customName = :customName, itemType = :itemType, amount = :amount, expireDate = :expireDate WHERE id = :id")
    fun updateFridgeItem(id: Int, customName: String, itemType: String, amount: Float, expireDate: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFridgeOrShoppingListItem(vararg fridgeItem: FridgeItemEntity)

    @Query("DELETE FROM FridgeItems WHERE id==:id")
    fun deleteFridgeOrShoppingListItem(id: Int)

    // Recipes

    @Query("SELECT * FROM Recipes")
    fun getAllRecipes(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(vararg recipe: RecipeEntity)

    @Query("UPDATE Recipes SET recipeUrl = :recipeUrl WHERE id = :id")
    fun updateRecipe(id: Int, recipeUrl: String)

    @Query("UPDATE FridgeItems SET isInFridge = 1 WHERE id = :id")
    fun moveFromShoppingListToFridge(id: Int)

    @Query("SELECT * FROM RecipeIngredients")
    fun getAllIngredientsAllRecipes(): List<RecipeIngredientEntity>

    @Query("SELECT * FROM RecipeIngredients WHERE recipeId == :id")
    fun getAllIngredientsForRecipe(vararg id: Int): List<RecipeIngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeIngredient(vararg recipe: RecipeIngredientEntity)

    @Query("DELETE FROM RecipeIngredients")
    fun deleteAllIngredientsAllRecipes()

    @Query("DELETE FROM Recipes")
    fun deleteAllRecipes()

    @Query("DELETE FROM Recipes WHERE id==:id")
    fun deleteRecipe(id: Int)

    @Query("DELETE FROM RecipeIngredients WHERE recipeId==:id")
    fun deleteRecipeIngredients(id: Int)

    @Transaction
    fun deleteRecipeAndItsIngredients(id : Int) {
        deleteRecipe(id)
        deleteRecipeIngredients(id) // kustutabki k??ik selle recipega seotud sest mitu t??kki on selle id-ga
    }

}
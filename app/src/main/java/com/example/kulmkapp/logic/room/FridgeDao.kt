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

    @Query("SELECT * FROM FridgeItems WHERE id==:itemId")
    fun getSingleFridgeItemById(itemId: Int): FridgeItemEntity

    @Query("SELECT * FROM FridgeItems WHERE isInFridge==0")
    fun getAllShoppingListItems(): List<FridgeItemEntity>

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

    @Query("SELECT * FROM RecipeIngredients WHERE recipeId == :id")
    fun getAllIngredientsForRecipe(vararg id: Int): List<RecipeIngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeIngredient(vararg recipe: RecipeIngredientEntity)

    @Query("DELETE FROM Recipes WHERE id==:id")
    fun deleteRecipe(id: Int)

    @Query("DELETE FROM RecipeIngredients WHERE recipeId==:id")
    fun deleteRecipeIngredients(id: Int)


    @Transaction
    fun deleteRecipeAndItsIngredients(id : Int) {
        deleteRecipe(id)
        deleteRecipeIngredients(id) // kustutabki kõik selle recipega seotud sest mitu tükki on selle id-ga
    }

    // KeyValue

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeyValue(vararg keyValue: KeyValueEntity)

    @Query("DELETE FROM KeyValue WHERE `key`==:KEY")
    fun deleteKeyValue(KEY: String)

    @Query("UPDATE KeyValue SET value = :VALUE WHERE `key` = :KEY")
    fun updateKeyValue(KEY: String, VALUE: String)

    @Query("SELECT COUNT(value) FROM KeyValue WHERE `key` = :KEY")
    fun getHowManyRowsKeyValue(KEY: String) : Int

    @Transaction
    fun addKeyValue(keyValue: KeyValueEntity){ // kontrollib kas selline key on juba tabelis ja kui on siis kirjutab üle, vastasel juhul lisab
        if (getHowManyRowsKeyValue(keyValue.key) == 0) {
            insertKeyValue(keyValue)
            Log.i("MyFridgeDAO", "Inserting new key ${keyValue.key} with value ${keyValue.value}")
        } else {
            updateKeyValue(keyValue.key, keyValue.value)
            Log.i("MyFridgeDAO", "Updating key ${keyValue.key} with value ${keyValue.value}")
        }
    }

    @Query("SELECT * FROM KeyValue WHERE `key`==:KEY")
    fun getValueByKey(KEY: String): KeyValueEntity


}
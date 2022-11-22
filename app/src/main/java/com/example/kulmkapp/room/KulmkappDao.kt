package com.example.kulmkapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KulmkappDao {
    @Query("SELECT * FROM Ingredients WHERE id==:ingredientId")
    fun loadSingleIngredientById(ingredientId: Int): IngredientEntity

    @Query("SELECT * FROM Ingredients WHERE name like :ingredientName || '%'")
    fun loadIngredientsLikeName(ingredientName: String): IngredientEntity

    @Query("SELECT * FROM Ingredients")
    fun loadAllIngredients(): IngredientEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKulmkappItem(vararg kulmkappItem: KulmkappItemEntity)

}
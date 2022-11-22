package com.example.kulmkapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    //val usedIngredients: List<IngredientEntity>, // tee uus table recipeingredients
    //val missedIngredients: List<IngredientEntity>,
    val imgUrl: String?
) {
    constructor() : this(0, "", "")
}
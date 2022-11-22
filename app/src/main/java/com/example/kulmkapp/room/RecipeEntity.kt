package com.example.kulmkapp.room

import androidx.room.Entity

@Entity(tableName = "Recipes")
class RecipeEntity(
    val id: Int,
    val title: String,
    val usedIngredients: List<IngredientEntity>,
    val missedIngredients: List<IngredientEntity>,
    val imgUrl: String?
) {
    constructor() : this(0, "", arrayListOf(), arrayListOf(), "")
}
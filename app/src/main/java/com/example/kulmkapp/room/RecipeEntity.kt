package com.example.kulmkapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    // uues tablis RecipeIngredients on sellesama id järgi ingrediendi id
    val imgUrl: String?,
    val recipeUrl: String?
) {
    constructor() : this(0, "", "", "")
}
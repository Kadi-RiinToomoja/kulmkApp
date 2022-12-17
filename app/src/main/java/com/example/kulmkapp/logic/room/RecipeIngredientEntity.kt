package com.example.kulmkapp.logic.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeIngredients")
class RecipeIngredientEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var ingredientId: Int,
    var recipeId: Int,
    var usesIngredient: Boolean
        ){
    constructor() : this(0,0,0, true)
}
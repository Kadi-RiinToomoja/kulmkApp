package com.example.kulmkapp.logic.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeIngredients")
class RecipeIngredientEntity (
    @PrimaryKey
    var id: Int,
    var recipeId: Int
        ){
    constructor() : this(0,0)
}
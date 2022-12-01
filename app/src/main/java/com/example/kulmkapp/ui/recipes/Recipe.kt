package com.example.kulmkapp.ui.recipes

import com.example.kulmkapp.room.IngredientEntity

data class Recipe(var recipeId: Int,
                  var recipeName: String,
                  var imageUrl: String,
                  var usedIngredients: MutableList<IngredientEntity>,
                  var missedIngredients: MutableList<IngredientEntity>)
{
}
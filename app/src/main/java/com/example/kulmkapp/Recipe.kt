package com.example.kulmkapp

data class Recipe(var recipeId: Int,
                  var imageUrl: String,
                  var recipeName: String,
                  var usedIngredients: List<String>,
                  var missedIngredients: List<String>)
{
}
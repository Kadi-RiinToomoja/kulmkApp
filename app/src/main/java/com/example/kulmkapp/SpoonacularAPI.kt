package com.example.kulmkapp

import android.content.Context
import android.util.Log
import com.koushikdutta.ion.Ion

object SpoonacularAPI {

    fun getRecipes(context: Context) {
        Ion.with(context)
            .load("https://api.spoonacular.com/recipes/findByIngredients")
            .setHeader("x-api-key", "40a02b1a97644f4aaf69b9f2902f0b5b")
                //?ingredients=apples,+flour,+sugar&number=2
            .addQuery("ingredients", "apples,+flour,+sugar")
            .addQuery("number", "2")
            .asJsonArray()
            .setCallback { e, result ->
                if(e!=null){
                    Log.e("Error", "Something was wrong! ${e.message}")
                } else {
                    var recipes = mutableListOf<Recipe>()
                    result.forEach {
                        var recipeId = it.asJsonObject.get("id").asInt
                        var imageUrl = it.asJsonObject.get("image").asString
                        var recipeName = it.asJsonObject.get("title").asString

                        var missedIngredients = mutableListOf<String>()
                        it.asJsonObject.get("missedIngredients").asJsonArray.forEach { ingredient ->
                            missedIngredients.add(ingredient.asJsonObject.get("name").asString)
                        }

                        var usedIngredients = mutableListOf<String>()
                        it.asJsonObject.get("usedIngredients").asJsonArray.forEach { ingredient ->
                            usedIngredients.add(ingredient.asJsonObject.get("name").asString)
                        }

                        recipes.add(Recipe(recipeId, imageUrl, recipeName, usedIngredients, missedIngredients))
                    }
                    recipes.forEach {

                    }
                    Log.d("SpoonacularResponse", recipes.toString())

                }
            }
    }
}


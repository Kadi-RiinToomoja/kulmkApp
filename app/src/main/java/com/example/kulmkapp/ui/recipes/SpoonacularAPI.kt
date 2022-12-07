package com.example.kulmkapp.ui.recipes

import android.content.Context
import android.util.Log
import com.example.kulmkapp.room.IngredientEntity
import com.example.kulmkapp.room.LocalRoomDb
import com.example.kulmkapp.room.RecipeEntity
import com.example.kulmkapp.room.RecipeIngredientEntity
import com.koushikdutta.ion.Ion

object SpoonacularAPI {

    fun getRecipes(context: Context, query: String, db: LocalRoomDb) {
        Ion.with(context)
            .load("https://api.spoonacular.com/recipes/findByIngredients")
            .setHeader("x-api-key", "40a02b1a97644f4aaf69b9f2902f0b5b")
            //?ingredients=apples,+flour,+sugar&number=2
            .addQuery("ingredients", query)
            .addQuery("number", "2")
            .asJsonArray()
            .setCallback { e, result ->
                if (e != null) {
                    Log.e("Error", "Something was wrong! ${e.message}")
                } else {
                    var ids: MutableList<Int> = mutableListOf()
                    result.forEach {
                        var recipeId = it.asJsonObject.get("id").asInt
                        ids.add(recipeId)
                        var recipeName = it.asJsonObject.get("title").asString

                        var usedIngredients = mutableListOf<IngredientEntity>()
                        it.asJsonObject.get("usedIngredients").asJsonArray.forEach { ingredient ->
                            usedIngredients.add(
                                IngredientEntity(
                                    ingredient.asJsonObject.get("id").asInt,
                                    ingredient.asJsonObject.get("name").asString
                                )
                            )
                        }

                        var missedIngredients = mutableListOf<IngredientEntity>()
                        it.asJsonObject.get("missedIngredients").asJsonArray.forEach { ingredient ->
                            missedIngredients.add(
                                IngredientEntity(
                                    ingredient.asJsonObject.get("id").asInt,
                                    ingredient.asJsonObject.get("name").asString
                                )
                            )
                        }

                        var imageUrl = it.asJsonObject.get("image").asString

                        // insert data
                        db.getKulmkappDao()
                            .insertRecipe(RecipeEntity(recipeId, recipeName, imageUrl, null))

                        usedIngredients.forEach { item ->
                            db.getKulmkappDao().insertIngredient(item)
                            db.getKulmkappDao()
                                .insertRecipeIngredient(RecipeIngredientEntity(recipeId, item.id))

                        }
                        missedIngredients.forEach { item ->
                            db.getKulmkappDao().insertIngredient(item)
                            db.getKulmkappDao()
                                .insertRecipeIngredient(RecipeIngredientEntity(recipeId, item.id))
                        }

                        Log.d(
                            "SpoonacularResponse",
                            "$recipeId $recipeName"
                        )

                    }
                    // links to recipes
                    getRecipesSources(context, ids, db)
                }
            }
    }

    fun getRecipesSources(context: Context, ids: List<Int>, db: LocalRoomDb) {
        Ion.with(context)
            .load("https://api.spoonacular.com/recipes/informationBulk")
            .setHeader("x-api-key", "40a02b1a97644f4aaf69b9f2902f0b5b")
            //?ingredients=apples,+flour,+sugar&number=2
            .addQuery("ids", ids.joinToString(","))
            .asJsonArray()
            .setCallback { e, result ->
                if (e != null) {
                    Log.e("Error", "Something was wrong! ${e.message}")
                } else {
                    result.forEach {
                        var recipeId = it.asJsonObject.get("id").asInt
                        var recipeUrl = it.asJsonObject.get("sourceUrl").asString

                        // insert data
                        db.getKulmkappDao().updateRecipe(recipeId, recipeUrl)

                        Log.d(
                            "SpoonacularResponse link",
                            "$recipeId $recipeUrl"
                        )
                    }
                }
            }
    }

}


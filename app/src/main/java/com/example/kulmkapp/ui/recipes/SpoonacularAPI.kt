package com.example.kulmkapp.ui.recipes

import android.content.Context
import android.util.Log
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.RecipeEntity
import com.example.kulmkapp.logic.room.RecipeIngredientEntity
import com.koushikdutta.ion.Ion

object SpoonacularAPI {

    fun getRecipes(context: Context, query: String, dao: FridgeDao, adapter: RecipesAdapter) {
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
                    // get rid of old recipes
                    dao.deleteAllRecipes()
                    val ids: MutableList<Int> = mutableListOf()
                    result.forEach {
                        val recipeId = it.asJsonObject.get("id").asInt
                        ids.add(recipeId)
                        val recipeName = it.asJsonObject.get("title").asString

                        val usedIngredients = mutableListOf<IngredientEntity>()
                        it.asJsonObject.get("usedIngredients").asJsonArray.forEach { ingredient ->
                            usedIngredients.add(
                                IngredientEntity(
                                    ingredient.asJsonObject.get("id").asInt,
                                    ingredient.asJsonObject.get("name").asString
                                )
                            )
                        }

                        val missedIngredients = mutableListOf<IngredientEntity>()
                        it.asJsonObject.get("missedIngredients").asJsonArray.forEach { ingredient ->
                            missedIngredients.add(
                                IngredientEntity(
                                    ingredient.asJsonObject.get("id").asInt,
                                    ingredient.asJsonObject.get("name").asString
                                )
                            )
                        }

                        val imageUrl = it.asJsonObject.get("image").asString

                        // insert data
                        dao.insertRecipe(RecipeEntity(recipeId, recipeName, imageUrl, null))

                        usedIngredients.forEach { item ->
                            dao.insertIngredient(item)
                            dao.insertRecipeIngredient(RecipeIngredientEntity(recipeId, item.id))

                        }
                        missedIngredients.forEach { item ->
                            dao.insertIngredient(item)
                            dao.insertRecipeIngredient(RecipeIngredientEntity(recipeId, item.id))
                        }

                        Log.d(
                            "SpoonacularResponse",
                            "$recipeId $recipeName"
                        )

                    }
                    // links to recipes
                    getRecipesSources(context, ids, dao)
                    // notify changes
                    adapter.data = dao.getAllRecipes()
                    adapter.notifyDataSetChanged()
                }
            }
    }

    fun getRecipesSources(context: Context, ids: List<Int>, dao: FridgeDao) {
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
                        val recipeId = it.asJsonObject.get("id").asInt
                        val recipeUrl = it.asJsonObject.get("sourceUrl").asString

                        // insert data
                        dao.updateRecipe(recipeId, recipeUrl)

                        Log.d(
                            "SpoonacularResponse link",
                            "$recipeId $recipeUrl"
                        )
                    }
                }
            }
    }
}


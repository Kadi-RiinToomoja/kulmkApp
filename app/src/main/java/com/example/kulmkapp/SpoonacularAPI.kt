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
                    val cities = result
                    Log.d("Response", result.toString())
                    Log.d("Response", result.get(0).asJsonObject.get("missedIngredients").asJsonArray.toString())
                    Log.d("Response", result.get(0).asJsonObject.get("missedIngredients").asJsonArray.get(0).asJsonObject.get("name").asString)

                }
            }
    }
}
package com.example.kulmkapp.ui.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.example.kulmkapp.logic.room.RecipeEntity
import com.example.kulmkapp.logic.room.RecipeIngredientEntity

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    val db = LocalRoomDb.getInstance(application)

    var recipeArray: List<RecipeEntity> = listOf()
    var idArray: List<RecipeIngredientEntity> = listOf()
    var ingredientArray: List<IngredientEntity> = listOf()

    fun refresh() {
        // Reload dataset from DB, put it in in-memory list
        //recipeArray = listOf<RecipeEntity>(RecipeEntity(0, "example title", ""), RecipeEntity(1, "asfaf title", ""))
        recipeArray = db.getFridgeDao().getAllRecipes()
        idArray = db.getFridgeDao().getAllIngredientsAllRecipes()
        ingredientArray = db.getFridgeDao().getAllIngredients()
    }
}
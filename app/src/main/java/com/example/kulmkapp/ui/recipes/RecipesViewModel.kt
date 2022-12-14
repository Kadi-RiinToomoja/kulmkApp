package com.example.kulmkapp.ui.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.example.kulmkapp.logic.room.RecipeEntity

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    val db = LocalRoomDb.getInstance(application)

    var recipeArray: List<RecipeEntity> = listOf()

    fun refresh() {
        // Reload dataset from DB, put it in in-memory list
        //recipeArray = listOf<RecipeEntity>(RecipeEntity(0, "example title", ""), RecipeEntity(1, "asfaf title", ""))
        recipeArray = db.getFridgeDao().getAllRecipes()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is recipes Fragment"
    }
    val text: LiveData<String> = _text
}
package com.example.kulmkapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kulmkapp.room.LocalRoomDb
import com.example.kulmkapp.room.RecipeEntity

class RecipeViewModel(application: Application): AndroidViewModel(application) {

    val db = LocalRoomDb.getInstance(application)

    var recipeArray: List<RecipeEntity> = listOf()

    fun refresh(){
        // Reload dataset from DB, put it in in-memory list
        recipeArray = listOf<RecipeEntity>(RecipeEntity(0, "example title", ""))
        //recipeArray = db.getKulmkappDao().loadAllRecipes()
    }
}
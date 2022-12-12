package com.example.kulmkapp.ui.fridge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.example.kulmkapp.logic.room.RecipeEntity

class FridgeViewModel(application: Application) : AndroidViewModel(application) {

    val dao = LocalRoomDb.getInstance(application).getFridgeDao()

    var fridgeArray: List<FridgeItemEntity> = listOf()

    fun refresh() {
        // Reload dataset from DB, put it in in-memory list
        //recipeArray = listOf<RecipeEntity>(RecipeEntity(0, "example title", ""), RecipeEntity(1, "asfaf title", ""))
        fridgeArray = dao.loadAllFridgeItems()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is fridge Fragment"
    }
    val text: LiveData<String> = _text
}
package com.example.kulmkapp.logic

import android.app.Activity
import android.util.Log
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.FridgeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngredientsListReader(val activity : Activity, val dao: FridgeDao) {
    val TAG = "ReadIngredientsList"


    fun readIngredientsIfNeeded(){
        Log.i(TAG, "test reading ingredients list")

        val existingIngredientEntities = dao.getAllIngredients()
        if(existingIngredientEntities.size==0){
            Log.i(TAG, "reading ingredients")
            CoroutineScope(Dispatchers.IO).launch {
                readIngredients()
            }
        }
        else{
            Log.i(TAG, "ingredients table already has ingredients")
        }
    }

    private fun readIngredients() {
        val assets = activity.assets
        val filestream = assets.open("top1kingredients.csv")
        val lines = filestream.bufferedReader().readLines()

        lines.forEach {
            val (name, id) = it.split(';', ignoreCase = false, limit = 2)
            Log.i(TAG, name)

            val ingredientEntity = IngredientEntity(id.toInt(), name)
            dao.insertIngredient(ingredientEntity)
        }
        dao.insertIngredient(IngredientEntity(-1, "Other"))
        filestream.close()
    }
}


    

package com.example.kulmkapp.logic

import android.app.Activity
import android.content.res.AssetManager
import android.net.Uri
import android.os.ParcelFileDescriptor.open
import android.util.Log
import com.example.kulmkapp.R
import com.example.kulmkapp.room.IngredientEntity
import com.example.kulmkapp.room.KulmkappDao
import java.io.File
import java.io.InputStream

class IngredientsList(val activity : Activity, val dao: KulmkappDao) {
    val TAG = "read ingredients list"


    fun readIngredientsIfNeeded(){
        Log.i(TAG, "test reading ingredients list")

        val existingIngredientEntities = dao.loadAllIngredients()
        if(existingIngredientEntities.size!=0){
            Log.i(TAG, "reading ingredients")
            readIngredients()
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
    }
}


    

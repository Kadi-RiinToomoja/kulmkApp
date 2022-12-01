package com.example.kulmkapp.logic

import android.app.Activity
import android.content.res.AssetManager
import android.net.Uri
import android.os.ParcelFileDescriptor.open
import android.util.Log
import com.example.kulmkapp.R
import java.io.File
import java.io.InputStream

class IngredientsList(val activity : Activity) {
    val TAG = "read ingredients list"

    fun readIngredients(){
        Log.i(TAG, "test reading ingredients list")

        val assets = activity.assets
        val filestream = assets.open("top1kingredients.csv")
        val lines =  filestream.bufferedReader().readLines()

        lines.forEach {
            val (name, id) = it.split(';', ignoreCase = false, limit = 2)
            Log.i(TAG, name)
        }
    }}


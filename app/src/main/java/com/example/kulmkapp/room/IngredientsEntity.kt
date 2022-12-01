package com.example.kulmkapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Ingredients")
class IngredientsEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var type: String,
    //var expireDate: Date?
){
    // Companion objects are used for static definitions in Kotlin
    companion object { const val DATEFORMAT = "dd/MM/yyyy" }

    constructor() : this(0, "", "")
}

package com.example.kulmkapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Ingredients")
class IngredientEntity (
    @PrimaryKey
    var id: Int,
    var name: String,
){
    constructor() : this(0, "")
}

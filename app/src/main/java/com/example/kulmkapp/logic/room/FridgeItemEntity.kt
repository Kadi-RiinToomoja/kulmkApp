package com.example.kulmkapp.logic.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "FridgeItems")
class FridgeItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val customName: String,
    val idInList: Int, // null kui ei ole sellist asja
    val amount: Float?,
    val isInFridge: Int, // is in fridge 1, is not 0, doesn't go there -1
    val expireDate: Date?
) {
    constructor() : this(0, "", 0, 0f, 0, Date())
}
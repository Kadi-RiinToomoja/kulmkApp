package com.example.kulmkapp.logic.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "FridgeItems")
class FridgeItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val customName: String,
    val itemType: String, // "" kui ei ole sellist asja -> Other
    val amount: Float?,
    val isInFridge: Int, // is in fridge 1, is not 0, doesn't go there -1
    val expireDate: String?,
){
    override fun toString(): String {return customName}
}

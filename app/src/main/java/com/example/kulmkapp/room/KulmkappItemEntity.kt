package com.example.kulmkapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "KulmkappItems")
class KulmkappItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val nameInList: String,
    val idInList: Int,
    val amount: Float,
    val type: Int,
    val expireDate: Date?
) {
    constructor() : this(0, "", "", 0, 0f, 0, Date())
}
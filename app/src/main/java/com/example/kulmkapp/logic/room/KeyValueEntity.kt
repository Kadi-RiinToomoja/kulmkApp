package com.example.kulmkapp.logic.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "KeyValue")
class KeyValueEntity (
    @PrimaryKey
    var key: String,
    var value: String,
)
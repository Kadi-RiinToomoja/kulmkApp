package com.example.kulmkapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [IngredientsEntity::class], version = 1)
abstract  class LocalRoomDb : RoomDatabase() {

    // val db = LocalRoomDb.getInstance(someContext)

    abstract fun getIncredientDao(): KulmkappDao

    companion object {
        private lateinit var kulmkappDb : LocalRoomDb

        @Synchronized fun getInstance(context: Context) : LocalRoomDb {

            if (!this::kulmkappDb.isInitialized) {
                //Build the database
                kulmkappDb = Room.databaseBuilder(
                    context, LocalRoomDb::class.java, "Ingredients")
                    .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                    .allowMainThreadQueries() // if possible, use background thread instead
                    .build()
            }
            return kulmkappDb

        }
    }
}
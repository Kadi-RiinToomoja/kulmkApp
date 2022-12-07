package com.example.kulmkapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DateTypeConverter::class)
@Database(
    entities = [IngredientEntity::class, KulmkappItemEntity::class, RecipeEntity::class, RecipeIngredientEntity::class],
    version = 5
)
abstract class LocalRoomDb : RoomDatabase() {

    abstract fun getKulmkappDao(): KulmkappDao

    companion object {
        private lateinit var kulmkappDb: LocalRoomDb

        @Synchronized
        fun getInstance(context: Context): LocalRoomDb {

            if (!this::kulmkappDb.isInitialized) {
                //Build the database
                kulmkappDb = Room.databaseBuilder(
                    context, LocalRoomDb::class.java, "kulmkappDb"
                )
                    .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                    .allowMainThreadQueries() // if possible, use background thread instead
                    .build()
            }
            return kulmkappDb
        }
    }
}
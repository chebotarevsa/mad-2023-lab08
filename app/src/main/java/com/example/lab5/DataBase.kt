package com.example.lab5

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [CardTable::class], version = 7)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun funDao(): Dao

    companion object {
        @Volatile
        private var instance: DataBase? = null

        fun getInstance(context: Context): DataBase = instance ?: synchronized(this) {
            instance ?: getBuiltDatabase(context).also {
                instance = it
            }
        }

        private fun getBuiltDatabase(context: Context): DataBase = Room.databaseBuilder(
            context, DataBase::class.java, "cardDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}
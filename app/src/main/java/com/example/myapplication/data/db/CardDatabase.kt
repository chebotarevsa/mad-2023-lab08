package com.example.myapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.Converters

@Database(entities = [CardTable::class], version = 7)
@TypeConverters(Converters::class)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {

        @Volatile
        private var instance: CardDatabase? = null

        fun getInstance(context: Context): CardDatabase = instance ?: synchronized(this) {
            instance ?: getBuiltDatabase(context).also {
                instance = it
            }
        }

        private fun getBuiltDatabase(context: Context): CardDatabase = Room.databaseBuilder(
            context, CardDatabase::class.java, "cardDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}
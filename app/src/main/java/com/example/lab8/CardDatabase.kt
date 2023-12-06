package com.example.lab8

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Card::class], version = 2)
@TypeConverters(Converters::class)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        private var cardDatabase: CardDatabase? = null
        fun getInstance(context: Context): CardDatabase {
            synchronized(this) {
                var databaseInstance = cardDatabase
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(
                        context, CardDatabase::class.java, "cardDatabase"
                    ).fallbackToDestructiveMigration().build()
                    cardDatabase = databaseInstance
                }
                return databaseInstance
            }
        }
    }
}
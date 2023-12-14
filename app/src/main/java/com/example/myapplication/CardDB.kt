package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Card::class], version = 2)
@TypeConverters(Converters::class)
abstract class CardDB : RoomDatabase() {
    abstract fun cardDAO(): CardDAO

    companion object {
        private var cardDB: CardDB? = null
        fun getInstance(context: Context): CardDB {
            synchronized(this) {
                var databaseInstance = cardDB
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(
                        context, CardDB::class.java, "cardDB"
                    ).fallbackToDestructiveMigration().build()
                    cardDB = databaseInstance
                }
                return databaseInstance
            }
        }
    }
}
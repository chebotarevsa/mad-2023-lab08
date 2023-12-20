package com.example.lab8mobile.Data.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lab8mobile.BitmapConverter

@Database(entities = [TermCardFromDB::class], version = 2)
@TypeConverters(BitmapConverter::class)
abstract class TermCardDataBase : RoomDatabase() {

    abstract fun termCardDao(): TermCardDao

    companion object {
        @Volatile
        private var INSTANCE: TermCardDataBase? = null

        fun getDatabase(context: Context): TermCardDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TermCardDataBase::class.java,
                    "term_cards"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
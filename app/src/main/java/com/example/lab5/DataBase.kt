package com.example.lab5

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Card::class], version = 2)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun funDao(): Dao

    companion object {
        private var myDataBase: DataBase? = null
        fun getInstance(context: Context): DataBase {
            synchronized(this) {
                var databaseInstance = myDataBase
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(
                        context, DataBase::class.java, "myDatabase"
                    ).fallbackToDestructiveMigration().build()
                    myDataBase = databaseInstance
                }
                return databaseInstance
            }
        }
    }
}
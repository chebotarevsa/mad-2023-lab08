package com.example.lab5

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao {
    @Insert
    suspend fun put(vararg cards: Card)
    @Insert
    suspend fun put(card: Card)
    @Update
    suspend fun update(card: Card): Int
    @Delete
    suspend fun delete(card: Card): Int
    @Query("SELECT * FROM card")
    fun getAll(): LiveData<List<Card>>
    @Query("SELECT * FROM card WHERE translation=:translation LIMIT 1")
    fun getTranslation(translation: String): LiveData<Card>
    @Query("SELECT * FROM card WHERE id=:id LIMIT 1")
    fun getId(id: Int): LiveData<Card>
}
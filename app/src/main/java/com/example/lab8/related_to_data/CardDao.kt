package com.example.lab8.related_to_data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardDao {

    @Insert
    suspend fun insert(card: Card)

    @Insert
    suspend fun insert(cards: List<Card>)

    @Query("SELECT * FROM card")
    fun findAll(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE translation=:translation LIMIT 1")
    fun findByTranslation(translation: String): LiveData<Card>

    @Query("SELECT * FROM card WHERE id=:id LIMIT 1")
    fun findById(id: String): LiveData<Card>

    @Update
    suspend fun update(card: Card): Int

    @Delete
    suspend fun delete(card: Card): Int
}
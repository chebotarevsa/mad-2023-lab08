package com.example.lab8.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab8.db.entity.Card
import com.example.lab8.db.entity.CardEntity

@Dao
interface CardDao {
    @Insert
    suspend fun insert(vararg cards: CardEntity)

    @Insert
    suspend fun insert(card: CardEntity)

    @Insert
    suspend fun insert(card: List<CardEntity>)

    @Query("SELECT * FROM CardEntity")
    fun findAll(): LiveData<List<Card>>

    @Query("SELECT * FROM CardEntity WHERE translation=:translation LIMIT 1")
    fun findByTranslation(translation: String): LiveData<Card>

    @Query("SELECT * FROM CardEntity WHERE id=:id LIMIT 1")
    fun findById(id: String): LiveData<Card>

    @Update
    suspend fun update(card: CardEntity): Int

    @Delete
    suspend fun delete(card: CardEntity): Int
}
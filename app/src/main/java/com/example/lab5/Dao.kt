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
    suspend fun put(cardTable: CardTable)

    @Insert
    suspend fun put(cardTables: List<CardTable>)
    @Update
    suspend fun update(card: Card): Int
    @Delete
    suspend fun delete(card: Card): Int
    @Query("SELECT * FROM cardtable")
    fun getAll(): LiveData<List<CardTable>>
    @Query("SELECT * FROM cardtable WHERE translation=:translation LIMIT 1")
    fun findByTranslation(translation: String): LiveData<CardTable>

    @Query("SELECT * FROM cardtable WHERE id=:id LIMIT 1")
    fun findById(id: String): LiveData<CardTable?>

    @Update
    suspend fun update(cardTable: CardTable): Int

    @Delete
    suspend fun delete(cardTable: CardTable): Int
}
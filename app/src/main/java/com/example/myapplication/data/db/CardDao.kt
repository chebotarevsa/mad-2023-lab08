package com.example.myapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardDao {

    @Insert
    suspend fun insert(cardTable: CardTable)

    @Insert
    suspend fun insert(cardTables: List<CardTable>)

    @Query("SELECT * FROM cardtable")
    fun findAll(): LiveData<List<CardTable>>

    @Query("SELECT * FROM cardtable WHERE translation=:translation LIMIT 1")
    fun findByTranslation(translation: String): LiveData<CardTable>

    @Query("SELECT * FROM cardtable WHERE id=:id LIMIT 1")
    fun findById(id: String): LiveData<CardTable?>

    @Update
    suspend fun update(cardTable: CardTable): Int

    @Delete
    suspend fun delete(cardTable: CardTable): Int
}
package com.example.myapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface CardDAO {

    @Insert
    suspend fun insert(cardTable: CardTable)

    @Insert
    suspend fun insert(cardTable: List<CardTable>)

    @Update
    suspend fun update(cardTable: CardTable): Int

    @Delete
    suspend fun delete(cardTable: CardTable): Int

    @Query("SELECT * FROM cardtable")
    fun findAllCard(): LiveData<List<CardTable>>

    @Query("SELECT * FROM cardtable WHERE id=:id LIMIT 1")
    fun findById(id: String): LiveData<CardTable>

}
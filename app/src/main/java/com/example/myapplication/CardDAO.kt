package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface CardDAO {
    @Insert
    suspend fun insert(vararg card: Card)

    @Insert
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card): Int

    @Delete
    suspend fun delete(card: Card): Int

    @Query("SELECT * FROM card")
    fun findAllCard(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE id=:id LIMIT 1")
    fun findById(id: Int): LiveData<Card>

}
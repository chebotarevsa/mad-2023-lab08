package com.example.lab8mobile.Data.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TermCardDao {
    @Insert
    suspend fun insert(cards: List<TermCardFromDB>)

    @Insert
    suspend fun insert(card: TermCardFromDB)

    @Update
    suspend fun update(card: TermCardFromDB): Int

    @Delete
    suspend fun delete(card: TermCardFromDB): Int

    @Query("SELECT * FROM term_cards")
    fun getAll(): LiveData<List<TermCardFromDB>>

    @Query("SELECT * FROM term_cards WHERE id=:id LIMIT 1")
    fun getId(id: String): LiveData<TermCardFromDB>
}




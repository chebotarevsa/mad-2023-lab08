package com.example.lab8.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CardTableTagDao {
    @Query("SELECT * FROM CardTableTag")
    fun getAll(): List<CardTableTag>

    @Insert
    fun insert(cardTableTag: CardTableTag)

    @Delete
    fun delete(cardTableTag: CardTableTag)

    @Query("DELETE FROM CardTableTag WHERE card_table_id = :cardTableId")
    fun deleteByCardTableId(cardTableId: String)
}
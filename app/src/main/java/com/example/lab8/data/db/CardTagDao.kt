package com.example.lab8.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cardTag: CardTag)

    @Delete
    suspend fun delete(cardTag: CardTag)

    @Query("SELECT tagId FROM cardTag WHERE cardId = :cardId")
    fun getTagsForCard(cardId: String): LiveData<List<String>>

    @Query("SELECT cardId FROM cardTag WHERE tagId = :tagId")
    fun getCardsWithTag(tagId: String): LiveData<List<String>>
}

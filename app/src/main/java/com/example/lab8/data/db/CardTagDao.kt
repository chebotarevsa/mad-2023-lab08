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

    @Query("DELETE FROM cardtag WHERE cardId = :cardId ")
    suspend fun deleteByCardId(cardId: String)

    @Query("SELECT tag.* FROM tag INNER JOIN cardTag ON tag.id = cardTag.tagId WHERE cardTag.cardId = :cardId")
    fun getTagsForCardWithLiveData(cardId: String): LiveData<List<Tag>>

    @Query("SELECT tag.* FROM tag INNER JOIN cardTag ON tag.id = cardTag.tagId WHERE cardTag.cardId = :cardId")
    suspend fun  getTagsForCard(cardId: String): List<Tag>


    @Query("SELECT cardId FROM cardTag WHERE tagId = :tagId")
    fun getCardsWithTag(tagId: String): LiveData<List<String>>
}

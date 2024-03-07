package com.example.lab8.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8.data.client.CardModel
import com.example.lab8.data.db.Tag
import com.example.lab8.data.repository.CardRepositoryImpl
import com.example.lab8.domain.entity.Card
import okhttp3.ResponseBody

interface CardRepository {
    suspend fun loadCards()

    suspend fun getImage(fileName: String): ResponseBody

    suspend fun getCards(): List<CardModel>

    suspend fun insert(card: Card, tag: Tag)

    suspend fun insert(cards: List<Card>)

    fun findAll(): LiveData<List<Card>>

    fun findById(id: String): LiveData<Card>

    suspend fun update(card: Card, tag: Tag, tagsForCard: List<Tag>)

    suspend fun delete(card: Card)

    suspend fun getCardsWithTagWithLiveData(tagId: String): LiveData<List<String>>

    fun getTagsForCardWithLiveData(cardId: String): LiveData<List<Tag>>

    suspend fun getTagsForCard(cardId: String): List<Tag>
    fun findCardsByTagNameLike(tagName: String): LiveData<List<Card>>

    companion object {

        fun getInstance(application: Application): CardRepository {
            return CardRepositoryImpl.getInstance(application)
        }
    }
}
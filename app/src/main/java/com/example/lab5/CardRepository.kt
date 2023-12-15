package com.example.lab5

import android.app.Application
import androidx.lifecycle.LiveData
import okhttp3.ResponseBody

interface CardRepository {

    suspend fun loadCards()

    suspend fun getImage(fileName: String): ResponseBody

    suspend fun getCards(): List<CardModel>

    suspend fun put(card: Card)

    suspend fun put(cards: List<Card>)

    fun getAll(): LiveData<List<Card>>

    fun getId(id: String): LiveData<Card>

    suspend fun update(card: Card): Int

    suspend fun delete(card: Card): Int

    companion object {

        fun getInstance(application: Application): CardRepository {
            return CardRepositoryI.getInstance(application)
        }
    }
}
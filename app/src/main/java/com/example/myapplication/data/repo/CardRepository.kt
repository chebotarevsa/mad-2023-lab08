package com.example.myapplication.data.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myapplication.data.Card
import com.example.myapplication.data.remote.CardModel
import okhttp3.ResponseBody

interface CardRepository {
    suspend fun loadCards()

    suspend fun getImage(fileName: String): ResponseBody

    suspend fun getCards(): List<CardModel>

    suspend fun insert(card: Card)

    suspend fun insert(cards: List<Card>)

    fun findAllCard(): LiveData<List<Card>>

    fun findById(id: String): LiveData<Card>

    suspend fun update(card: Card): Int

    suspend fun delete(card: Card): Int

    companion object {

        fun getInstance(application: Application): CardRepository {
            return CardRepositoryImpl.getInstance(application)
        }
    }
}
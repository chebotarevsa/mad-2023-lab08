package com.example.lab8.db.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8.db.entity.Card
import com.example.lab8.web.dto.CardDto
import okhttp3.ResponseBody

interface CardRepository {

    suspend fun loadCards()

    suspend fun getImage(fileName: String): ResponseBody

    suspend fun getCards(): List<CardDto>

    suspend fun insert(card: Card)

    suspend fun insert(cards: List<Card>)

    fun findAll(): LiveData<List<Card>>

    fun findById(id: String): LiveData<Card>

    suspend fun update(card: Card): Int

    suspend fun delete(card: Card): Int

    companion object {

        fun getInstance(application: Application): CardRepository {
            return CardRepositoryImpl.getInstance(application)
        }
    }
}
package com.example.lab8.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8.data.repository.CardRepositoryImpl
import com.example.lab8.domain.entity.Card

interface CardRepository {
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
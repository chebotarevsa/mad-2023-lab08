package com.example.lab8.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8.data.db.CardTable
import com.example.lab8.data.remote.CardModel
import com.example.lab8.data.repository.CardRepositoryImpl
import okhttp3.ResponseBody

interface CardRepository {

    suspend fun loadCards()

    suspend fun getImage(fileName: String): ResponseBody

    suspend fun getCards(): List<CardModel>

    suspend fun insert(cardTable: CardTable)

    suspend fun insert(cardTables: List<CardTable>)

    fun findAll(): LiveData<List<CardTable>>

    fun findById(id: String): LiveData<CardTable>

    suspend fun update(cardTable: CardTable): Int

    suspend fun delete(cardTable: CardTable): Int

    companion object {

        fun getInstance(application: Application): CardRepository {
            return CardRepositoryImpl.getInstance(application)
        }
    }
}
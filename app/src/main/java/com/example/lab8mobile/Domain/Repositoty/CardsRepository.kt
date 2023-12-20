package com.example.lab8mobile.Domain.Repositoty

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8mobile.Data.Net.TermCardModel
import com.example.lab8mobile.Data.Repository.CardsRepositoryImpl
import com.example.lab8mobile.Domain.Entity.TermCard
import okhttp3.ResponseBody

interface CardsRepository {

    suspend fun insert(cardTable: TermCard)

    suspend fun insert(cardTables: List<TermCard>)

    fun findAll(): LiveData<List<TermCard>>

    fun findById(id: String): LiveData<TermCard>

    suspend fun update(cardTable: TermCard): Int

    suspend fun delete(cardTable: TermCard): Int


    suspend fun loadCards()

    suspend fun getImage(fileName: String): ResponseBody

    suspend fun getCards(): List<TermCardModel>

    companion object {

        fun getInstance(application: Application): CardsRepository {
            return CardsRepositoryImpl.getInstance(application)
        }
    }
}
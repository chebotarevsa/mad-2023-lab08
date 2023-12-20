package com.example.lab8mobile.Data.DB

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8mobile.Data.TermCard

interface CardsRepositoryDB {

    suspend fun insert(cardTable: TermCard)

    suspend fun insert(cardTables: List<TermCard>)

    fun findAll(): LiveData<List<TermCard>>

    fun findById(id: String): LiveData<TermCard>

    suspend fun update(cardTable: TermCard): Int

    suspend fun delete(cardTable: TermCard): Int



    companion object {

        fun getInstance(application: Application): CardsRepositoryDB {
            return CardsRepositoryDBImpl.getInstance(application)
        }
    }
}
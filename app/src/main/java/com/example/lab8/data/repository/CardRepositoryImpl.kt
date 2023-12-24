package com.example.lab8.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.lab8.data.db.CardDao
import com.example.lab8.data.db.CardDatabase
import com.example.lab8.data.db.toDb
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository

class CardRepositoryImpl private constructor(
    private val cardDao: CardDao
) : CardRepository {

    override suspend fun insert(card: Card) =
        cardDao.insert(card.toDb())

    override suspend fun insert(cards: List<Card>) =
        cardDao.insert(cards.map { it.toDb() })

    override fun findAll(): LiveData<List<Card>> =
        cardDao.findAll().map {
            it.map {
                Card(
                    id = it.id,
                    question = it.question,
                    example = it.example,
                    answer = it.answer,
                    translation = it.translation,
                    image = it.image
                )
            }
        }

    override fun findById(id: String): LiveData<Card> =
        cardDao.findById(id).map {
            Card(
                id = it?.id ?: "empty" ,
                question = it?.question ?: "",
                example = it?.example?: "",
                answer = it?.answer?: "",
                translation = it?.translation?: "",
                image = it?.image
            )
        }

    override suspend fun update(card: Card): Int =
        cardDao.update(card.toDb())

    override suspend fun delete(card: Card): Int =
        cardDao.delete(card.toDb())

    companion object {

        @Volatile
        private var instance: CardRepositoryImpl? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepositoryImpl(
                    CardDatabase.getInstance(application).cardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
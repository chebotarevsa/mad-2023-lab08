package com.example.lab8.db.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.lab8.db.dao.CardDao
import com.example.lab8.db.entity.Card
import com.example.lab8.util.DbConnection
import com.example.lab8.util.toDb

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

    override fun findById(id: String): LiveData<Card> {
        if (id == "-1") {
            val emptyCardLiveData = MutableLiveData<Card>()
            emptyCardLiveData.value = Card("", "", "", "", "", null)
            return emptyCardLiveData
        }
        return cardDao.findById(id).map {
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
                    DbConnection.getInstance(application).cardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
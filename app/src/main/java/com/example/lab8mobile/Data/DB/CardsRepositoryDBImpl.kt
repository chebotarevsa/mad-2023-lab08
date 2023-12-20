package com.example.lab8mobile.Data.DB

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.lab8mobile.Data.TermCard

class CardsRepositoryDBImpl(private val termCardDao: TermCardDao) : CardsRepositoryDB {


    override suspend fun insert(termCard: TermCard) =
        termCardDao.insert(termCard.toDb())

    override suspend fun insert(termCards: List<TermCard>) {
        termCardDao.insert(termCards.map {
            it.toDb()
        })
    }

    override fun findAll(): LiveData<List<TermCard>> =
        termCardDao.getAll().map {
            it.map {
                TermCard(
                    id = it.id,
                    question = it.question,
                    example = it.example,
                    answer = it.answer,
                    translate = it.translate,
                    image = it.image
                )
            }
        }

    override fun findById(id: String): LiveData<TermCard> {
        if (id == "-1") {
            val emptyCardLiveData = MutableLiveData<TermCard>()
            emptyCardLiveData.value = TermCard("", "", "", "", "", null)
            return emptyCardLiveData
        } else {
            return termCardDao.getId(id).map {
                TermCard(
                    id = it.id,
                    question = it.question,
                    example = it.example,
                    answer = it.answer,
                    translate = it.translate,
                    image = it.image
                )
            }
        }
    }


    override suspend fun update(termCard: TermCard): Int =
        termCardDao.update(termCard.toDb())

    override suspend fun delete(termCard: TermCard): Int =
        termCardDao.delete(termCard.toDb())

    companion object {

        @Volatile
        private var instance: CardsRepositoryDBImpl? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardsRepositoryDBImpl(
                    TermCardDataBase.getDatabase(application).termCardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
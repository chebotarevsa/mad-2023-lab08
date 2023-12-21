package com.example.lab8.db.repository

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.lab8.db.dao.CardDao
import com.example.lab8.db.entity.Card
import com.example.lab8.db.entity.CardEntity
import com.example.lab8.util.DbConnection
import com.example.lab8.util.toDb
import com.example.lab8.web.client.CardClient
import com.example.lab8.web.client.ImageClient
import com.example.lab8.web.dto.CardDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

class CardRepositoryImpl private constructor(
    private val cardClient: CardClient,
    private val imageClient: ImageClient,
    private val cardDao: CardDao
) : CardRepository {

    override suspend fun loadCards() {
        val cardsFromRemote = cardClient.getCards().map {
            val imageBytes = imageClient.getImage(it.image).bytes()
            val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            CardEntity(
                id = it.id,
                question = it.question,
                example = it.example,
                answer = it.answer,
                translation = it.translation,
                image = imageBitmap
            )
        }
        cardDao.insert(cardsFromRemote)
    }

    @GET
    @Streaming
    override suspend fun getImage(@Url fileName: String): ResponseBody =
        imageClient.getImage(fileName)

    @GET("api.json")
    override suspend fun getCards(): List<CardDto> =
        cardClient.getCards()

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
        private const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepositoryImpl(
                    CardClient.getInstance(baseUrl),
                    ImageClient.getInstance(baseUrl),
                    DbConnection.getInstance(application).cardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
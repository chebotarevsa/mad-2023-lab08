package com.example.lab8.data.repository

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.lab8.data.client.CardClient
import com.example.lab8.data.client.CardModel
import com.example.lab8.data.client.ImageClient
import com.example.lab8.data.db.CardDao
import com.example.lab8.data.db.CardDatabase
import com.example.lab8.data.db.CardTable
import com.example.lab8.data.db.toDb
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository
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
            CardTable(
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

    @Streaming
    @GET
    override suspend fun getImage(@Url fileName: String): ResponseBody =
        imageClient.getImage(fileName)

    @GET("api.json")
    override suspend fun getCards(): List<CardModel> =
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
        private const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepositoryImpl(
                    CardClient.getInstance(baseUrl),
                    ImageClient.getInstance(baseUrl),
                    CardDatabase.getInstance(application).cardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
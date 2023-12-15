package com.example.lab5

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.util.UUID

class CardRepositoryI private constructor(
    private val cardController: CardController,
    private val imageController: ImageController,
    private val cardDao: Dao
) : CardRepository {

    override suspend fun loadCards() {
        val cardsFromRemote = cardController.getCards().map {
            val imageBytes = imageController.getImage(it.image).bytes()
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
        cardDao.put(cardsFromRemote)
    }

    @Streaming
    @GET
    override suspend fun getImage(@Url fileName: String): ResponseBody =
        imageController.getImage(fileName)

    @GET("api.json")
    override suspend fun getCards(): List<CardModel> =
        cardController.getCards()

    override suspend fun put(card: Card) =
        cardDao.put(card.toDb())

    override suspend fun put(cards: List<Card>) =
        cardDao.put(cards.map { it.toDb() })

    override fun getAll(): LiveData<List<Card>> =
        cardDao.getAll().map {
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

    override fun getId(id: String): LiveData<Card> =
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
        private var instance: CardRepositoryI? = null
        private const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepositoryI(
                    CardController.getInstance(baseUrl),
                    ImageController.getInstance(baseUrl),
                    DataBase.getInstance(application).funDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
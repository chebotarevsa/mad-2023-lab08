package com.example.myapplication.data.repo

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.myapplication.data.Card
import com.example.myapplication.data.db.CardDAO
import com.example.myapplication.data.db.CardDB
import com.example.myapplication.data.db.CardTable
import com.example.myapplication.data.db.toDb
import com.example.myapplication.data.remote.CardController
import com.example.myapplication.data.remote.CardModel
import com.example.myapplication.data.remote.ImgController
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

class CardRepositoryImpl private constructor(
    private val cardController: CardController,
    private val imageController: ImgController,
    private val cardDao: CardDAO
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
        cardDao.insert(cardsFromRemote)
    }

    @Streaming
    @GET
    override suspend fun getImage(@Url fileName: String): ResponseBody =
        imageController.getImage(fileName)

    @GET("api.json")
    override suspend fun getCards(): List<CardModel> = cardController.getCards()

    override suspend fun insert(card: Card) = cardDao.insert(card.toDb())

    override suspend fun insert(cards: List<Card>) = cardDao.insert(cards.map { it.toDb() })

    override fun findAllCard(): LiveData<List<Card>> = cardDao.findAllCard().map { it ->
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

    override fun findById(id: String): LiveData<Card> = cardDao.findById(id).map {
        Card(
            id = it.id,
            question = it.question,
            example = it.example,
            answer = it.answer,
            translation = it.translation,
            image = it.image
        )
    }

    override suspend fun update(card: Card): Int = cardDao.update(card.toDb())

    override suspend fun delete(card: Card): Int = cardDao.delete(card.toDb())

    companion object {

        @Volatile
        private var instance: CardRepositoryImpl? = null
        private const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"

        fun getInstance(application: Application) = instance ?: synchronized(this) {
            instance ?: CardRepositoryImpl(
                CardController.getInstance(baseUrl),
                ImgController.getInstance(baseUrl),
                CardDB.getInstance(application).cardDAO()
            ).also {
                instance = it
            }
        }
    }
}
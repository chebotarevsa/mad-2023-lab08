package com.example.lab8.data.repository

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.example.lab8.data.db.CardDao
import com.example.lab8.data.db.CardDatabase
import com.example.lab8.data.db.CardTable
import com.example.lab8.data.remote.CardController
import com.example.lab8.data.remote.CardModel
import com.example.lab8.data.remote.ImageController
import com.example.lab8.domain.repository.CardRepository
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

class CardRepositoryImpl private constructor(
    private val cardController: CardController,
    private val imageController: ImageController,
    private val cardDao: CardDao
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
    override suspend fun getCards(): List<CardModel> =
        cardController.getCards()

    override suspend fun insert(cardTable: CardTable) =
        cardDao.insert(cardTable)

    override suspend fun insert(cardTables: List<CardTable>) =
        cardDao.insert(cardTables)

    override fun findAll(): LiveData<List<CardTable>> =
        cardDao.findAll()

    override fun findById(id: String): LiveData<CardTable> =
        cardDao.findById(id)

    override suspend fun update(cardTable: CardTable): Int =
        cardDao.update(cardTable)

    override suspend fun delete(cardTable: CardTable): Int =
        cardDao.delete(cardTable)

    companion object {

        @Volatile
        private var instance: CardRepositoryImpl? = null
        private const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepositoryImpl(
                    CardController.getInstance(baseUrl),
                    ImageController.getInstance(baseUrl),
                    CardDatabase.getInstance(application).cardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
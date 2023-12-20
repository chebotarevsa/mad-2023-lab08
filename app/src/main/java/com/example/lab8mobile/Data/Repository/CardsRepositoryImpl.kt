package com.example.lab8mobile.Data.Repository

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.lab8mobile.Data.DB.TermCardDao
import com.example.lab8mobile.Data.DB.TermCardDataBase
import com.example.lab8mobile.Data.DB.TermCardFromDB
import com.example.lab8mobile.Data.DB.toDb
import com.example.lab8mobile.Data.Net.ImageController
import com.example.lab8mobile.Data.Net.TermCardController
import com.example.lab8mobile.Data.Net.TermCardModel
import com.example.lab8mobile.Domain.Repositoty.CardsRepository
import com.example.lab8mobile.Domain.Entity.TermCard
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

class CardsRepositoryImpl(
    private val cardController: TermCardController,
    private val imageController: ImageController,
    private val termCardDao: TermCardDao
) : CardsRepository {


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

    override suspend fun loadCards() {
        val cardsFromRemote = cardController.getCards().map {
            val imageBytes = imageController.getImage(it.image).bytes()
            val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            TermCardFromDB(
                id = it.id,
                question = it.question,
                example = it.example,
                answer = it.answer,
                translate = it.translation,
                image = imageBitmap
            )
        }
        termCardDao.insert(cardsFromRemote)
    }

    @Streaming
    @GET
    override suspend fun getImage(@Url fileName: String): ResponseBody =
        imageController.getImage(fileName)


    @GET("api.json")
    override suspend fun getCards(): List<TermCardModel> =
        cardController.getCards()


    companion object {

        @Volatile
        private var instance: CardsRepositoryImpl? = null
        private const val url = "https://osa-s3.agroinvest.com/test/lab08/"

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardsRepositoryImpl(
                    TermCardController.getInstance(url),
                    ImageController.getInstance(url),
                    TermCardDataBase.getDatabase(application).termCardDao()
                )
                    .also {
                        instance = it
                    }
            }
    }
}
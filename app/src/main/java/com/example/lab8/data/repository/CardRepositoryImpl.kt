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
import com.example.lab8.data.db.CardTag
import com.example.lab8.data.db.CardTagDao
import com.example.lab8.data.db.Tag
import com.example.lab8.data.db.TagDao
import com.example.lab8.data.db.toDb
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.util.UUID

class CardRepositoryImpl private constructor(
    private val cardClient: CardClient,
    private val imageClient: ImageClient,
    private val cardDao: CardDao,
    private val tagDao: TagDao,
    private val cardTagDao: CardTagDao
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

    override suspend fun insert(card: Card, tag: Tag) {
        val cardTable = card.toDb()
        cardDao.insert(cardTable)
        cardTagDao.insert(CardTag(UUID.randomUUID().toString(), cardTable.id, tag.id))
    }

    override suspend fun insert(cards: List<Card>) =
        cardDao.insert(cards.map { it.toDb() })

    override suspend fun update(card: Card, tag: Tag, tagsForCard: List<Tag>) {
        val cardTable = card.toDb()
        cardDao.update(cardTable)
        cardTagDao.insert(CardTag(UUID.randomUUID().toString(), card.id, tag.id))
    }


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
                id = it?.id ?: "empty",
                question = it?.question ?: "",
                example = it?.example ?: "",
                answer = it?.answer ?: "",
                translation = it?.translation ?: "",
                image = it?.image
            )
        }


    override suspend fun delete(card: Card) {
        cardDao.delete(card.toDb())
        cardTagDao.deleteByCardId(card.id)
    }

    override fun getTagsForCardWithLiveData(cardId: String): LiveData<List<Tag>> =
        cardTagDao.getTagsForCardWithLiveData(cardId)

    override suspend fun getTagsForCard(cardId: String): List<Tag> =
        cardTagDao.getTagsForCard(cardId)

    override fun findCardsByTagNameLike(tagName: String): LiveData<List<Card>> =
        cardDao.findCardsByTagName(tagName).map {
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


    override suspend fun getCardsWithTagWithLiveData(tagId: String): LiveData<List<String>> =
        cardTagDao.getCardsWithTag(tagId)


    companion object {

        @Volatile
        private var instance: CardRepositoryImpl? = null
        private const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepositoryImpl(
                    CardClient.getInstance(baseUrl),
                    ImageClient.getInstance(baseUrl),
                    CardDatabase.getInstance(application).cardDao(),
                    CardDatabase.getInstance(application).tagDao(),
                    CardDatabase.getInstance(application).cardTagDao(),
                )
                    .also {
                        instance = it
                    }
            }
    }
}
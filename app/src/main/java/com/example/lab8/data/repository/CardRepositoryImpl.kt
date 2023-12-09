package com.example.lab8.data.repository

import android.graphics.BitmapFactory
import com.example.lab8.data.db.CardDatabase
import com.example.lab8.data.db.toDb
import com.example.lab8.data.remote.CardController
import com.example.lab8.data.remote.ImageController
import com.example.lab8.data.remote.baseUrl
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository
import retrofit2.Retrofit

class CardRepositoryImpl(
    private val cardDatabase: CardDatabase,
    private val cardController: CardController,
    private val imageController: ImageController
) : CardRepository {
    override suspend fun loadCards() {
        val cardsFromRemote = cardController.getCards().map {
            val imageBytes = imageController.getImage(it.image).bytes()
            val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Card(
                id = it.id,
                question = it.question,
                example = it.example,
                answer = it.answer,
                translation = it.translation,
                image = imageBitmap
            ).toDb()
        }
        cardDatabase.cardDao().insert(cardsFromRemote)
    }
}
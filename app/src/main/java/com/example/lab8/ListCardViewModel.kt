package com.example.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.CardDatabase
import com.example.lab8.data.db.CardTable
import com.example.lab8.data.remote.CardController
import com.example.lab8.data.remote.ImageController
import com.example.lab8.data.repository.CardRepositoryImpl
import com.example.lab8.domain.repository.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ListCardViewModel(
    private val database: CardDatabase,
    private val cardRepository: CardRepository
) : ViewModel() {
    var cards: LiveData<List<CardTable>> = database.cardDao().findAll()

    fun deleteCard(cardId: String) {
        thread {
            val card = cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { database.cardDao().delete(it) } }
        }
    }

    fun getCardsFromRemoteIfEmpty() {
        if (cards.value!!.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                cardRepository.loadCards()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ListCardViewModel(
                    CardDatabase.getInstance(application),
                    CardRepositoryImpl(
                        CardDatabase.getInstance(application),
                        CardController.getInstance(),
                        ImageController.getInstance()
                    )
                ) as T
            }
        }
    }
}
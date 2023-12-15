package com.example.lab5

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlinx.coroutines.Dispatchers

    class CardListViewModel(private val cardRepository: CardRepository) : ViewModel() {
            var list_cards: LiveData<List<Card>> = cardRepository.getAll()
        fun deleteCard(cardId: String) {
        thread {
            val card = list_cards.value?.first { it.id == cardId }
            card?.let {
                viewModelScope.launch {
                    cardRepository.delete(it)
                }
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
                return CardListViewModel(CardRepository.getInstance(application)) as T
            }
        }
    }
            fun getCardsFromRemoteIfEmpty() {
        if (list_cards.value!!.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                cardRepository.loadCards()
            }
        }
    }
}
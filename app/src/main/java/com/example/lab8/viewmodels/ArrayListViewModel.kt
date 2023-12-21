package com.example.lab8.viewmodels

import com.example.lab8.db.repository.CardRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.db.entity.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ArrayListViewModel(private val cardRepository: CardRepository) : ViewModel() {

    val cards: LiveData<List<Card>> = cardRepository.findAll()

    fun getCardsFromRemoteIfEmpty() {
        if (cards.value!!.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                cardRepository.loadCards()
            }
        }
    }

    fun deleteCard(cardId: String) {
        thread {
            val card = cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { cardRepository.delete(it) } }
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
                return ArrayListViewModel(CardRepository.getInstance(application)) as T
            }
        }
    }
}
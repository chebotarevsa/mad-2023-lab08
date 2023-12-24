package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.domain.entity.Card
import com.example.myapplication.domain.repository.CardRepository
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class CardListViewModel(private val cardRepository: CardRepository) : ViewModel() {

    var cards: LiveData<List<Card>> = cardRepository.findAll()

    fun deleteCard(cardId: String) {
        thread {
            val card = cards.value?.first { it.id == cardId }
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
                return CardListViewModel(
                    CardRepository.getInstance(application)
                ) as T
            }
        }
    }
}
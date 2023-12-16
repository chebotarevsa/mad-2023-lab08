package com.example.lab5

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

    class CardSeeViewModel(cardRepository: CardRepository, cardId: String) : ViewModel() {
        val card: LiveData<Card> = cardRepository.getId(cardId)

    companion object {
                    fun Factory(cardId: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
    return CardSeeViewModel(CardRepository.getInstance(application), cardId) as T
            }
        }
    }
}
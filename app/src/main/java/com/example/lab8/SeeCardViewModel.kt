package com.example.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class SeeCardViewModel(database: CardDatabase, cardId: Int) : ViewModel() {
    val card: LiveData<Card> = database.cardDao().findById(cardId)

    companion object {
        fun Factory(cardId: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return SeeCardViewModel(CardDatabase.getInstance(application), cardId) as T
            }
        }
    }
}

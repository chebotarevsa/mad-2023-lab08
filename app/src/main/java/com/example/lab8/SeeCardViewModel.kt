package com.example.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.related_to_data.Card
import com.example.lab8.related_to_data.CardDatabase

class SeeCardViewModel(database: CardDatabase, cardId: String) : ViewModel() {
    val card: LiveData<Card> = database.cardDao().findById(cardId)

    companion object {
        fun Factory(cardId: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
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

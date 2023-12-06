package com.example.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ListCardViewModel(private val database: CardDatabase) : ViewModel() {
    var cards: LiveData<List<Card>> = database.cardDao().findAll()

    fun deleteCard(cardId: Int) {
        thread {
            val card = cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { database.cardDao().delete(it) } }
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
                return ListCardViewModel(CardDatabase.getInstance(application)) as T
            }
        }
    }
}
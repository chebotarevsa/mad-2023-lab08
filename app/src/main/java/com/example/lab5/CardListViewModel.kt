package com.example.lab5

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class CardListViewModel(private val database: DataBase) : ViewModel() {
    var list_cards: LiveData<List<Card>> = database.funDao().getAll()
        fun deleteCard(cardId: Int) {
        thread {
            val card = list_cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { database.funDao().delete(it) } }
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
                return CardListViewModel(DataBase.getInstance(application)) as T
            }
        }
    }
}
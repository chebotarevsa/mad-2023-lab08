package com.example.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.related_to_data.Card
import com.example.lab8.related_to_data.CardDatabase
import com.example.lab8.related_to_data.RetrofitController
import com.example.lab8.related_to_data.RetrofitHelper
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ListCardViewModel(private val database: CardDatabase) : ViewModel() {
    var cards: LiveData<List<Card>> = database.cardDao().findAll()
    val retrofitController = RetrofitHelper.getInstance().create(RetrofitController::class.java)

    fun deleteCard(cardId: String) {
        thread {
            val card = cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { database.cardDao().delete(it) } }
        }
    }

    fun getCardsFromRemoteIfEmpty() {
        if (cards.value!!.isEmpty()) {
            viewModelScope.launch {
                database.cardDao().insert(retrofitController.getCards())
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
                return ListCardViewModel(CardDatabase.getInstance(application)) as T
            }
        }
    }
}
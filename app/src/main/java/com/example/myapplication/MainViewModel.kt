package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainViewModel(private val database: CardDB) : ViewModel() {
    var cards: LiveData<List<Card>> = database.cardDAO().findAllCard()
    private var _card = MutableLiveData<Card>()
    val card: LiveData<Card> = _card

    fun deleteCardById(cardId: Int) {
        thread {
            val card = cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { database.cardDAO().delete(it) } }
        }
    }

    fun setCardToDelete(cardId: Int) {
        _card.value = database.cardDAO().findById(cardId).value
    }

    fun setCardList() {
        cards = database.cardDAO().findAllCard()
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MainViewModel(CardDB.getInstance(application)) as T
            }
        }
    }
}
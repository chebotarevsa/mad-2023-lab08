package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.data.Card
import com.example.myapplication.data.repo.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainViewModel(private val cardRepository: CardRepository) : ViewModel() {
    var cards: LiveData<List<Card>> = cardRepository.findAllCard()
    private var _cardTable = MutableLiveData<Card>()
    val card: LiveData<Card> = _cardTable

    fun deleteCardById(cardId: String) {
        thread {
            val card = cards.value?.first { it.id == cardId }
            card?.let { viewModelScope.launch { cardRepository.delete(it) } }
        }
    }

    fun setCardToDelete(cardId: String) {
        _cardTable.value = cardRepository.findById(cardId).value
    }

    fun setCardList() {
        cards = cardRepository.findAllCard()
    }

    fun getFromRemote() {
        if (cards.value!!.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                cardRepository.loadCards()
            }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MainViewModel(CardRepository.getInstance(application)) as T
            }
        }
    }
}
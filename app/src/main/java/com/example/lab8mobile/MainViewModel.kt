package com.example.lab8mobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8mobile.Domain.Repositoty.CardsRepository
import com.example.lab8mobile.Domain.Entity.TermCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CardsRepository) : ViewModel() {

    var cardsList: LiveData<List<TermCard>> = repository.findAll()


    fun deleteCard(card: TermCard) {
        viewModelScope.launch {
            repository.delete(card)
        }
    }


     var isGetCardsExecuted = false

    fun getCardsFromRemoteIfEmpty() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isGetCardsExecuted) {
                repository.loadCards()
                isGetCardsExecuted = true
            }
        }
    }


    companion object {
        fun Factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application =
                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    return MainViewModel(CardsRepository.getInstance(application)) as T
                }
            }
    }
}
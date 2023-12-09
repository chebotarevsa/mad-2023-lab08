package com.example.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.CardTable
import com.example.lab8.domain.repository.CardRepository

class SeeCardViewModel(cardRepository: CardRepository, cardId: String) : ViewModel() {

    val cardTable: LiveData<CardTable> = cardRepository.findById(cardId)

    companion object {

        fun Factory(cardId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application =
                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    return SeeCardViewModel(CardRepository.getInstance(application), cardId) as T
                }
            }
    }
}

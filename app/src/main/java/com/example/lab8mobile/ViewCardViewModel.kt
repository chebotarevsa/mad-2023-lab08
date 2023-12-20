package com.example.lab8mobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8mobile.Domain.Repositoty.CardsRepository
import com.example.lab8mobile.Domain.Entity.TermCard

class ViewCardViewModel(private val repositoryDB: CardsRepository, val cardId : String ) : ViewModel()  {


    val card: LiveData<TermCard> = repositoryDB.findById(cardId)


    companion object {
        fun Factory(cardId: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ViewCardViewModel(CardsRepository.getInstance(application), cardId) as T
            }
        }
    }
}



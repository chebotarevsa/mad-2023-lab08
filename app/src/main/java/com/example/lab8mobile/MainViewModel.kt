package com.example.lab8mobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8mobile.Data.DB.CardsRepositoryDB
import com.example.lab8mobile.Data.TermCard
import kotlinx.coroutines.launch

class MainViewModel(private val repositoryDB: CardsRepositoryDB) : ViewModel() {

    var cardsList: LiveData<List<TermCard>> = repositoryDB.findAll()


    fun deleteCard(card: TermCard) {
        viewModelScope.launch {
            repositoryDB.delete(card)
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    return MainViewModel(CardsRepositoryDB.getInstance(application)) as T
                }
            }
    }
}
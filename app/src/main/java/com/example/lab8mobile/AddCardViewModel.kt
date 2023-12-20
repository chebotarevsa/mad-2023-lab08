package com.example.lab8mobile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8mobile.Domain.Repositoty.CardsRepository
import com.example.lab8mobile.Domain.Entity.TermCard
import kotlinx.coroutines.launch
import java.util.UUID

class AddCardViewModel(private val repositoryDB: CardsRepository, val cardId: String) :
    ViewModel() {

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> get() = _imageBitmap

    private val _repoCard = repositoryDB.findById(cardId)
    private val _card = MediatorLiveData<TermCard>()
    val card: LiveData<TermCard> = _card

    fun setImageBitmap(bitmap: Bitmap?) {
        _imageBitmap.value = bitmap
    }

    init {
        _card.addSource(_repoCard) {
            if (cardId != NEW_CARD) _card.value = it
            else _card.value = getEmptyCard()
        }
    }

    private fun getEmptyCard() =
        TermCard(
            id = UUID.randomUUID().toString(),
            question = "",
            example = "",
            answer = "",
            translate = "",
            image = null
        )

    fun addOrUpdateCard(
        question: String,
        example: String,
        answer: String,
        translate: String,
    ) {
        val image = imageBitmap.value
        val newCard = card.value?.copy(
            question = question,
            example = example,
            answer = answer,
            translate = translate,
            image = image
        )
        newCard?.let {
            viewModelScope.launch {
                if (cardId == NEW_CARD) {
                    repositoryDB.insert(it)
                } else {
                    repositoryDB.update(it)
                }
            }
        }
    }

    override fun onCleared() {
        _card.removeSource(_repoCard)
        super.onCleared()
    }


    companion object {
        fun Factory(cardId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    return AddCardViewModel(CardsRepository.getInstance(application), cardId) as T
                }
            }
    }
}
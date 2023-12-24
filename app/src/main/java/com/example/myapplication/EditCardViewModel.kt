package com.example.myapplication

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.domain.entity.Card
import com.example.myapplication.domain.repository.CardRepository
import kotlinx.coroutines.launch


class EditCardViewModel(private val cardRepository: CardRepository, val cardId: String) :
    ViewModel() {

    private val _repoCard = cardRepository.findById(cardId)


    private val _card = MediatorLiveData<Card>()
    val card: LiveData<Card> = _card

    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _card.addSource(_repoCard) {
            if (!checkIfNewCard()) _card.value = it
            else _card.value = getEmptyCard()
        }
    }

    private fun getEmptyCard() =
        Card(question = "", example = "", translation = "", answer = "")

    fun checkIfNewCard() = cardId == "-1"

    fun saveCard(
        question: String, example: String, answer: String, translation: String
    ) {
        val image = image.value
        val newCard = card.value?.copy(
            question = question,
            example = example,
            answer = answer,
            translation = translation,
            image = image
        )
        newCard?.let {
            viewModelScope.launch {
                if (checkIfNewCard()) {
                    cardRepository.insert(it)
                } else {
                    cardRepository.update(it)
                }
            }
        }
    }

    fun setImage(image: Bitmap?) {
        _image.value = image
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
                    return EditCardViewModel(CardRepository.getInstance(application), cardId) as T
                }
            }
    }
}
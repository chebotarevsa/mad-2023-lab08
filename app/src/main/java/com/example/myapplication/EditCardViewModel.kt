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
import com.example.myapplication.data.Card
import com.example.myapplication.data.repo.CardRepository
import kotlinx.coroutines.launch

class EditCardViewModel(private val cardRepository: CardRepository, cardId: String) : ViewModel() {
    private var _cardTable = MediatorLiveData<Card>()
    val cardTable: LiveData<Card> = _cardTable
    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _cardTable.addSource(cardRepository.findById(cardId)) {
            _cardTable.value = it
        }
    }

    fun editCard(
        cardId: String, question: String, example: String, answer: String, translation: String
    ) {
        viewModelScope.launch {
            cardRepository.update(Card(cardId, question, example, answer, translation, image.value))
        }
    }

    fun setImageToCard(image: Bitmap?) {
        _image.value = image
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
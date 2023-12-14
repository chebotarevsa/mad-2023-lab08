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
import kotlinx.coroutines.launch

class EditCardViewModel(private val database: CardDB, val cardId: Int) : ViewModel() {
    private var _card = MediatorLiveData<Card>()
    val card: LiveData<Card> = _card
    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _card.addSource(database.cardDAO().findById(cardId)) {
            _card.value = it
        }
    }

    fun editCard(
        cardId: Int, question: String, example: String, answer: String, translation: String
    ) {
        viewModelScope.launch {
            database.cardDAO()
                .update(Card(cardId, question, example, answer, translation, image.value))
        }
    }

    fun setImageToCard(image: Bitmap?) {
        _image.value = image
    }

    companion object {
        fun Factory(cardId: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return EditCardViewModel(CardDB.getInstance(application), cardId) as T
            }
        }
    }
}
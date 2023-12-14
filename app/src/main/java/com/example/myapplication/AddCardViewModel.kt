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
import androidx.room.Database
import kotlinx.coroutines.launch

class AddCardViewModel(private val database: CardDB) : ViewModel() {
    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image
    private val _card = MediatorLiveData<Card>()
    val card: LiveData<Card> = _card

    init {
        _card.value = Card(null, "", "", "", "", image.value)
    }

    fun addCard(
        question: String, example: String, answer: String, translation: String
    ) {
        viewModelScope.launch {
            database.cardDAO()
                .insert(Card(null, question, example, answer, translation, image.value))
        }
    }

    fun setImageToCard(image: Bitmap?) {
        _image.value = image
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return AddCardViewModel(CardDB.getInstance(application)) as T
            }
        }
    }
}
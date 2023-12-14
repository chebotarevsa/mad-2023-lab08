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

class AddCardViewModel(private val cardRepository: CardRepository) : ViewModel() {
    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image
    private val _cardTable = MediatorLiveData<Card>()
    val cardTable: LiveData<Card> = _cardTable

    init {
        _cardTable.value =
            Card(question = "", example = "", answer = "", translation = "", image = null)
    }

    fun addCard(
        question: String, example: String, answer: String, translation: String
    ) {
        viewModelScope.launch {
            cardRepository.insert(
                Card(
                    question = question,
                    example = example,
                    answer = answer,
                    translation = translation,
                    image = image.value
                )
            )
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
                return AddCardViewModel(CardRepository.getInstance(application)) as T
            }
        }
    }
}
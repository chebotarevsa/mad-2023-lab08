package com.example.lab8

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.related_to_data.Card
import com.example.lab8.related_to_data.CardDatabase
import kotlinx.coroutines.launch


class EditCardViewModel(private val database: CardDatabase, val cardId: String) : ViewModel() {
    private val _dbCard = database.cardDao().findById(cardId)

    private var _currentCard: Card? = null

    private val _card = MediatorLiveData<Card>()
    val card: LiveData<Card> = _card


    private var _questionError = MutableLiveData<String>()
    val questionError: LiveData<String> = _questionError
    private var _exampleError = MutableLiveData<String>()
    val exampleError: LiveData<String> = _exampleError
    private var _answerError = MutableLiveData<String>()
    val answerError: LiveData<String> = _answerError
    private var _translationError = MutableLiveData<String>()
    val translationError: LiveData<String> = _translationError

    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _card.addSource(_dbCard) {
            if (!checkIfNewCard()) _card.value = it
            else _card.value = getEmptyCard()
        }
    }

    fun setImage(image: Bitmap?) {
        _image.value = image
    }

    fun validateQuestion(question: String) {
        if (question.isBlank() && (_currentCard?.question ?: "").isNotBlank()) {
            _questionError.value = "Error"
        }
        if (question != card.value?.question) {
            _currentCard = card.value?.copy(question = question)
        }
    }

    fun validateExample(example: String) {
        if (example.isBlank() && (_currentCard?.example ?: "").isNotBlank()) {
            _exampleError.value = "Error"
        }
        if (example != card.value?.example) {
            _currentCard = card.value?.copy(example = example)
        }
    }

    fun validateAnswer(answer: String) {
        if (answer.isBlank() && (_currentCard?.answer ?: "").isNotBlank()) {
            _answerError.value = "Error"
        }
        if (answer != card.value?.answer) {
            _currentCard = card.value?.copy(answer = answer)
        }
    }

    fun validateTranslation(translation: String) {
        if (translation.isBlank() && (_currentCard?.translation ?: "").isNotBlank()) {
            _translationError.value = "Error"
        }
        if (translation != card.value?.translation) {
            _currentCard = card.value?.copy(translation = translation)
        }
    }

    fun saveCard(
        question: String, example: String, answer: String, translation: String
    ) {
        val image = image.value
        if (checkAllIfNotBlank(question, example, answer, translation)) {
            _status.value = Failed("One or several fields are blank")
        } else {
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
                        database.cardDao().insert(it)
                    } else {
                        database.cardDao().update(it)
                    }
                    _status.value = Success()
                }
            }
        }
    }

    private fun getEmptyCard() = Card(question = "", example = "", translation = "", answer = "")
    fun checkIfNewCard() = cardId == "empty"

    private fun checkAllIfNotBlank(
        question: String,
        example: String,
        answer: String,
        translation: String,
    ) = question.isBlank() || example.isBlank() || answer.isBlank() || translation.isBlank()

    override fun onCleared() {
        _card.removeSource(_dbCard)
        super.onCleared()
    }

    companion object {
        fun Factory(cardId: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return EditCardViewModel(CardDatabase.getInstance(application), cardId) as T
            }
        }
    }
}
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
import com.example.lab8.data.db.CardTable
import com.example.lab8.domain.repository.CardRepository
import kotlinx.coroutines.launch

class EditCardViewModel(private val cardRepository: CardRepository, val cardId: String) :
    ViewModel() {

    private val _repoCard = cardRepository.findById(cardId)

    private var _currentCardTable: CardTable? = null

    private val _cardTable = MediatorLiveData<CardTable>()
    val cardTable: LiveData<CardTable> = _cardTable

    private var _questionError = MutableLiveData<String>()
    val questionError: LiveData<String> = _questionError
    private var _exampleError = MutableLiveData<String>()
    val exampleError: LiveData<String> = _exampleError
    private var _answerError = MutableLiveData<String>()
    val answerError: LiveData<String> = _answerError
    private var _translationError = MutableLiveData<String>()
    val translationError: LiveData<String> = _translationError
    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    init {
        _cardTable.addSource(_repoCard) {
            if (!checkIfNewCard()) _cardTable.value = it
            else _cardTable.value = getEmptyCard()
        }
    }

    fun setImage(image: Bitmap?) {
        _image.value = image
    }

    fun validateQuestion(question: String) {
        if (question.isBlank() && (_currentCardTable?.question ?: "").isNotBlank()) {
            _questionError.value = "Error"
        }
        if (question != cardTable.value?.question) {
            _currentCardTable = cardTable.value?.copy(question = question)
        }
    }

    fun validateExample(example: String) {
        if (example.isBlank() && (_currentCardTable?.example ?: "").isNotBlank()) {
            _exampleError.value = "Error"
        }
        if (example != cardTable.value?.example) {
            _currentCardTable = cardTable.value?.copy(example = example)
        }
    }

    fun validateAnswer(answer: String) {
        if (answer.isBlank() && (_currentCardTable?.answer ?: "").isNotBlank()) {
            _answerError.value = "Error"
        }
        if (answer != cardTable.value?.answer) {
            _currentCardTable = cardTable.value?.copy(answer = answer)
        }
    }

    fun validateTranslation(translation: String) {
        if (translation.isBlank() && (_currentCardTable?.translation ?: "").isNotBlank()) {
            _translationError.value = "Error"
        }
        if (translation != cardTable.value?.translation) {
            _currentCardTable = cardTable.value?.copy(translation = translation)
        }
    }

    fun saveCard(
        question: String, example: String, answer: String, translation: String
    ) {
        val image = image.value
        if (checkAllIfNotBlank(question, example, answer, translation)) {
            _status.value = Failed("One or several fields are blank")
        } else {
            val newCard = cardTable.value?.copy(
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
                    _status.value = Success()
                }
            }
        }
    }

    private fun getEmptyCard() =
        CardTable(question = "", example = "", translation = "", answer = "")

    fun checkIfNewCard() = cardId == "empty"

    private fun checkAllIfNotBlank(
        question: String,
        example: String,
        answer: String,
        translation: String,
    ) = question.isBlank() || example.isBlank() || answer.isBlank() || translation.isBlank()

    override fun onCleared() {
        _cardTable.removeSource(_repoCard)
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
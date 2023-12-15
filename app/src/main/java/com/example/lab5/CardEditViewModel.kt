package com.example.lab5

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class CardEditViewModel(private val database: DataBase, val cardId: Int) : ViewModel() {
    private val databaseCard = database.funDao().getId(cardId)
    private var _currentCard: Card? = null
    private val Mcard = MediatorLiveData<Card>()
    val сard: LiveData<Card> = Mcard
    private var Mquestion_error = MutableLiveData<String>()
    val question_error: LiveData<String> = Mquestion_error
    private var Mexample_error = MutableLiveData<String>()
    val example_error: LiveData<String> = Mexample_error
    private var Manswer_error = MutableLiveData<String>()
    val answer_error: LiveData<String> = Manswer_error
    private var Mtranslation_error = MutableLiveData<String>()
    val translation_error: LiveData<String> = Mtranslation_error

    private var Mstatus = MutableLiveData<Status>()
    val status: LiveData<Status> = Mstatus
    private var Mimage = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = Mimage


    fun setImage(image: Bitmap?) {
        Mimage.value = image
    }

    fun validateQuestion(question: String) {

        if (question.isBlank() && (_currentCard?.question ?: "").isNotBlank()) {
            Mquestion_error.value = "Ошибка"
        }
        if (question != сard.value?.question) {
            _currentCard = сard.value?.copy(question = question)
        }
    }

    fun validateExample(example: String) {
        if (example.isBlank() && (_currentCard?.example ?: "").isNotBlank()) {
            Mexample_error.value = "Ошибка"
        }
        if (example != сard.value?.example) {
            _currentCard = сard.value?.copy(example = example)
        }
    }

    fun validateAnswer(answer: String) {
        if (answer.isBlank() && (_currentCard?.answer ?: "").isNotBlank()) {
            Manswer_error.value = "Ошибка"
        }
        if (answer != сard.value?.answer) {
            _currentCard = сard.value?.copy(answer = answer)
        }
    }

    fun validateTranslation(translation: String) {
        if (translation.isBlank() && (_currentCard?.translation ?: "").isNotBlank()) {
            Mtranslation_error.value = "Ошибка"
        }
        if (translation != сard.value?.translation) {
            _currentCard = сard.value?.copy(translation = translation)
        }
    }

    init {
        Mcard.addSource(databaseCard) {
            if (!checkIfNewCard()) Mcard.value = it
            else Mcard.value = getEmptyCard()
        }
    }


    fun saveCard(
        question: String, example: String, answer: String, translation: String
    ) {
        val image = image.value
        if (checkAllIfNotBlank(question, example, answer, translation)) {
            Mstatus.value = Failed("One or several fields are blank")
        } else {
            val newCard = сard.value?.copy(
                question = question,
                example = example,
                answer = answer,
                translation = translation,
                image = image
            )
            newCard?.let {
                viewModelScope.launch {
                    if (checkIfNewCard()) {
                        database.funDao().put(it)
                    } else {
                        database.funDao().update(it)
                    }
                    Mstatus.value = Success()
                }
            }
        }
    }

    private fun getEmptyCard() = Card(null, "", "", "", "")
    fun checkIfNewCard() = cardId == -1

    private fun checkAllIfNotBlank(
        question: String,
        example: String,
        answer: String,
        translation: String,
    ) = question.isBlank() || example.isBlank() || answer.isBlank() || translation.isBlank()

    override fun onCleared() {
        Mcard.removeSource(databaseCard)
        super.onCleared()
    }

    companion object {
        fun Factory(cardId: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return CardEditViewModel(DataBase.getInstance(application), cardId) as T
            }
        }
    }

}
package com.example.lab8.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.Tag
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository
import com.example.lab8.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditCardViewModel(
    private val cardRepository: CardRepository,
    private val tagRepository: TagRepository,
    val cardId: String
) :
    ViewModel() {

    private val _repoCard = cardRepository.findById(cardId)

    val tagNames = tagRepository.findAllNames()

    private var _tags = listOf<Tag>()

    private val _card = MediatorLiveData<Card>()
    val card: LiveData<Card> = _card

    private var _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _card.addSource(_repoCard) {
            if (!checkIfNewCard()) _card.value = it
            else _card.value = getEmptyCard()
        }
        viewModelScope.launch(Dispatchers.IO) {
            _tags = cardRepository.getTagsForCard(cardId)
        }
    }

    private fun getEmptyCard() =
        Card(question = "", example = "", translation = "", answer = "")

    fun checkIfNewCard() = cardId == "-1"


    fun saveCard(
        question: String,
        example: String,
        answer: String,
        translation: String,
        selectedTagName: String
    ) {
        val image = image.value
        var selectedTag: Tag? = null
        viewModelScope.launch(Dispatchers.IO) {
            selectedTag = tagRepository.findByTagName(selectedTagName)

        }
        val newCard = card.value?.copy(
            question = question,
            example = example,
            answer = answer,
            translation = translation,
            image = image,

            )
        viewModelScope.launch(Dispatchers.IO) {
            if (checkIfNewCard()) {
                selectedTag?.let { cardRepository.insert(newCard!!, it) }
            } else {
                selectedTag?.let { cardRepository.update(newCard!!, it, _tags) }
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
                    return EditCardViewModel(
                        CardRepository.getInstance(application),
                        TagRepository.getInstance(application),
                        cardId
                    ) as T
                }
            }
    }
}


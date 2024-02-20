package com.example.lab8.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.Tag
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository
import com.example.lab8.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SeeCardViewModel(
    cardRepository: CardRepository,
    tagRepository: TagRepository,
    cardId: String
) : ViewModel() {

    val card: LiveData<Card> = cardRepository.findById(cardId)

    private var _tags = MutableLiveData<MutableList<Tag>>()
    val tags: LiveData<MutableList<Tag>> = _tags

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val tagIdsForCard = cardRepository.getTagsForCard(cardId).value
            tagIdsForCard?.forEach { tagId ->
                tagRepository.findById(tagId).value?.let { _tags.value?.add(it) }
            }
        }
    }

    companion object {

        fun Factory(cardId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application =
                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    return SeeCardViewModel(
                        CardRepository.getInstance(application),
                        TagRepository.getInstance(application),
                        cardId
                    ) as T
                }
            }
    }
}
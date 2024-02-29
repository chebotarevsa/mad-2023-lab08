package com.example.lab8.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.Tag
import com.example.lab8.domain.entity.Card
import com.example.lab8.domain.repository.CardRepository
import com.example.lab8.domain.repository.TagRepository


class SeeCardViewModel(
    private val cardRepository: CardRepository,
    private val tagRepository: TagRepository,
    private val cardId: String
) : ViewModel() {

    val card: LiveData<Card> = cardRepository.findById(cardId)
    private var _tags = cardRepository.getTagsForCardWithLiveData(cardId)
    val tags: LiveData<List<Tag>> = _tags

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
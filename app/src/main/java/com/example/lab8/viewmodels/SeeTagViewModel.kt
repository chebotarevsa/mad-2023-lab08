package com.example.lab8.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.Tag
import com.example.lab8.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SeeTagViewModel(private val tagRepository: TagRepository, private val tagId: String) :
    ViewModel() {

    val tag: LiveData<Tag> = tagRepository.findById(tagId)

    companion object {

        fun Factory(tagId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application =
                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    return SeeTagViewModel(TagRepository.getInstance(application), tagId) as T
                }
            }
    }
}
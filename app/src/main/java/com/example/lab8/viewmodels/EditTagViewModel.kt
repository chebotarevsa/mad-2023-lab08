package com.example.lab8.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.Tag
import com.example.lab8.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditTagViewModel(private val tagRepository: TagRepository, val tagId: String) :
    ViewModel() {

    private val _repoTag = tagRepository.findById(tagId)

    private val _tag = MediatorLiveData<Tag>()

    val tag: LiveData<Tag> = _tag

    init {
        _tag.addSource(_repoTag) {
            if (!checkIfNewTag()) _tag.value = it
            else _tag.value = getEmptyTag()
        }
    }
    private fun getEmptyTag() =
        Tag(tagName = "", colorCode = "#FFFFFF")

    fun checkIfNewTag() = tagId == "-1"


    fun saveTag(
        tagName: String, colorCode: String
    ) {
        val newTag = tag.value?.copy(
            tagName = tagName,
            colorCode = colorCode
        )
        newTag?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if (checkIfNewTag()) {
                    tagRepository.insert(it)
                } else {
                    tagRepository.update(it)
                }
            }
        }
    }

    override fun onCleared() {
        _tag.removeSource(_repoTag)
        super.onCleared()
    }
    companion object {

        fun Factory(tagId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>, extras: CreationExtras
                ): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    return EditTagViewModel(TagRepository.getInstance(application), tagId) as T
                }
            }
    }
}


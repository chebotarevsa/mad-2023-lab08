package com.example.lab8.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab8.data.db.Tag
import com.example.lab8.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class TagListViewModel(private val tagRepository: TagRepository) : ViewModel() {

    var tags: LiveData<List<Tag>> = tagRepository.findAll()

    fun findTagsLike(tagName: String) {
        tags = tagRepository.findByTagNameLike(tagName)
    }

    fun saveTags(tags: List<Tag>) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.insert(tags)
        }
    }

    fun deleteTag(tagId: String) {
        thread {
            val tag = tags.value?.first { it.id == tagId }
            tag?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    tagRepository.delete(it)
                }
            }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return TagListViewModel(
                    TagRepository.getInstance(application)
                ) as T
            }
        }
    }
}
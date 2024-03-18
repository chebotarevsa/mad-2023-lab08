package com.example.lab8.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8.data.db.Tag
import com.example.lab8.data.repository.TagRepositoryImpl


interface TagRepository {

    fun findAll(): LiveData<List<Tag>>
    fun findAllNames(): LiveData<List<String>>
    suspend fun delete(tag: Tag)

    suspend fun detach(tag: Tag)

    fun findByTagName(tagName: String): Tag

    fun findByTagNameLike(tagName: String): LiveData<List<Tag>>

    suspend fun insert(tag: Tag)

    suspend fun insert(tags: List<Tag>)

    fun findById(id: String): LiveData<Tag>

    suspend fun update(tag: Tag): Int
    suspend fun delete(tags: List<Tag>)


    companion object {

        fun getInstance(application: Application): TagRepository {
            return TagRepositoryImpl.getInstance(application)
        }
    }
}
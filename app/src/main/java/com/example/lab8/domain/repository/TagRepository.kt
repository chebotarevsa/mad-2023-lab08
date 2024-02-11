package com.example.lab8.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lab8.data.db.Tag
import com.example.lab8.data.repository.TagRepositoryImpl


interface TagRepository {

    fun findAll(): LiveData<List<Tag>>
    fun delete(tag: Tag)

    fun findByTagName(tagName: String): Tag

    suspend fun insert(tag: Tag)

    suspend fun insert(tags: List<Tag>)

    fun findById(id: String): LiveData<Tag>

    suspend fun update(tag: Tag): Int


    companion object {

        fun getInstance(application: Application): TagRepository {
            return TagRepositoryImpl.getInstance(application)
        }
    }
}
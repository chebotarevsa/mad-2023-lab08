package com.example.lab8.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TagDao {
    @Query("SELECT * FROM tag")
    fun getAll(): LiveData<List<Tag>>

    @Insert
    fun insert(tag: Tag)

    @Delete
    fun delete(tag: Tag)

    @Query("SELECT * FROM tag WHERE tagName = :tagName")
    fun findByTagName(tagName: String): Tag

    @Query("SELECT * FROM tag WHERE id = :id")
    fun findById(id: String): LiveData<Tag>
}

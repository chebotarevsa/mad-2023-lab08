package com.example.lab8.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Tag(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val tagName: String,
    val colorCode: String
)
package com.example.lab8.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class CardTag(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val cardId: String,
    val tagId: String
    )
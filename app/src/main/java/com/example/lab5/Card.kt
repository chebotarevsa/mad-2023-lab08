package com.example.lab5

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val question: String,
    val example: String,
    val answer: String,
    val translation: String,
    val image: Bitmap? = null
)

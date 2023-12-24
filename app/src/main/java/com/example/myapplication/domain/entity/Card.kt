package com.example.myapplication.domain.entity

import android.graphics.Bitmap
import java.util.UUID

data class Card(
    val id: String = UUID.randomUUID().toString(),
    val question: String,
    val example: String,
    val answer: String,
    val translation: String,
    val image: Bitmap? = null
)
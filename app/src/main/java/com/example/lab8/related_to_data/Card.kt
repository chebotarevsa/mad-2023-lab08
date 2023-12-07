package com.example.lab8.related_to_data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity
data class Card(
    @SerializedName("Id")
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("Question")
    val question: String,
    @SerializedName("Example")
    val example: String,
    @SerializedName("Answer")
    val answer: String,
    @SerializedName("Translate")
    val translation: String,
//    @SerializedName("Id")
    val image: Bitmap? = null
)

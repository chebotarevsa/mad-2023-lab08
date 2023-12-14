package com.example.myapplication.data.remote

import com.google.gson.annotations.SerializedName

data class CardModel(
    @SerializedName("Id") val id: String,
    @SerializedName("Question") val question: String,
    @SerializedName("Example") val example: String,
    @SerializedName("Answer") val answer: String,
    @SerializedName("Translate") val translation: String,
    @SerializedName("Image") val image: String
)

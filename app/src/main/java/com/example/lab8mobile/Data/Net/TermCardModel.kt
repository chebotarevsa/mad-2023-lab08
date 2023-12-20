package com.example.lab8mobile.Data.Net

import com.google.gson.annotations.SerializedName
data class TermCardModel(
    @SerializedName("Id") val id: String,
    @SerializedName("Question") val question: String,
    @SerializedName("Example") val example: String,
    @SerializedName("Answer") val answer: String,
    @SerializedName("Translate") val translation: String,
    @SerializedName("Image") val image: String
)

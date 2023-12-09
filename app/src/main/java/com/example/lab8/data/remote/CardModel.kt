package com.example.lab8.data.remote

import com.example.lab8.domain.entity.Card
import com.google.gson.annotations.SerializedName

data class CardModel(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Question")
    val question: String,
    @SerializedName("Example")
    val example: String,
    @SerializedName("Answer")
    val answer: String,
    @SerializedName("Translate")
    val translation: String,
    @SerializedName("Image")
    val image: String
) {
    fun toEntity(): Card {
        return Card(id, question, example, answer, translation)
    }
}
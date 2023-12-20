package com.example.lab8mobile.Data.DB

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lab8mobile.Domain.Entity.TermCard

@Entity(tableName = "term_cards")
data class TermCardFromDB(
    @PrimaryKey val id: String,
    val question: String,
    val example: String,
    val answer: String,
    val translate: String,
    val image: Bitmap?
)

fun TermCard.toDb(): TermCardFromDB =
    TermCardFromDB(id, question, example, answer, translate, image)



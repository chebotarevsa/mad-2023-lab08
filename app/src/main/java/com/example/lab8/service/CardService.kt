package com.example.lab8.service

import android.graphics.Bitmap
import com.example.lab8.db.entity.Card

object CardService {

    private val _cards = mutableListOf(
        Card(
            "0",
            "To make something bigger or longer",
            "Can you ... the ladder a bit?",
            "Extend",
            "Расширять"
        ), Card(
            "1",
            "The fact that something such as an animal or organism can exist in different forms",
            "Compounds that exist in more than one crystalline form are said to exhibit ...",
            "Polymorphism",
            "Полиморфизм"
        ), Card(
            "2",
            "An occasion when someone buys or sells something, or when money is exchanged or the activity of buying or selling something",
            "Each ... at the foreign exchange counter seems to take forever.",
            "Transaction",
            "Транзакция"
        ), Card(
            "3",
            "In an electronic transaction (= an operation that changes data), the fact of occurring either completely or not at all",
            "Database updates that require ...",
            "Atomicity",
            "Атомарность"
        )
    )
    val cards
        get() = _cards.toList()

    fun getCardById(id: String): Card =
        _cards.first { it.id == id }

    fun removeCard(id: String) {
        _cards.removeIf {
            it.id == id
        }
    }

    fun addCard(card: Card) {
        _cards.add(card)
    }

    fun updateCardList(card1: Card, card2: Card) {
        val num = _cards.indexOf(card1)
        _cards.remove(card1)
        _cards.add(num, card2)
    }

    fun updateCardList(id: String, card: Card) {
        _cards.remove(getCardById(id))
        _cards.add(card)
    }

    fun updateCard(
        oldCard: Card,
        question: String,
        example: String,
        answer: String,
        translation: String,
        image: Bitmap?
    ): Card {
        return oldCard.copy(
            id = oldCard.id,
            question = question,
            example = example,
            answer = answer,
            translation = translation,
            image = image
        )
    }

    fun createNewCard(
        question: String, example: String, answer: String, translation: String, image: Bitmap?
    ): Card {
        val nextId = _cards.maxBy { it.id }.id + 1
        return Card(nextId, question, example, answer, translation, image)
    }
}
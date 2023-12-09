package com.example.lab8.domain.repository

import com.example.lab8.domain.entity.Card

interface CardRepository {
    suspend fun loadCards()
}
package com.example.myapplication.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CardController {
    @GET("api.json")
    suspend fun getCards(): List<CardModel>

    companion object {
        fun getInstance(baseUrl: String): CardController =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build().create(CardController::class.java)
    }
}
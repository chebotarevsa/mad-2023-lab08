package com.example.lab8.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val baseUrl = "https://osa-s3.agroinvest.com/test/lab08/"

interface CardController {
    @GET("api.json")
    suspend fun getCards(): List<CardModel>

    companion object {
        fun getInstance(): CardController {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CardController::class.java)
        }
    }
}
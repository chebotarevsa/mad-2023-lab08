package com.example.lab8.web.client

import com.example.lab8.web.dto.CardDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CardClient {

    @GET("api.json")
    suspend fun getCards(): List<CardDto>

    companion object {

        fun getInstance(baseUrl: String): CardClient =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build().create(CardClient::class.java)
    }
}
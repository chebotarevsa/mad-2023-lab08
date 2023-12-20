package com.example.lab8mobile.Data.Net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TermCardController {
    @GET("api.json")
    suspend fun getCards(): List<TermCardModel>

    companion object {

        fun getInstance(baseUrl: String): TermCardController =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build().create(TermCardController::class.java)
    }
}
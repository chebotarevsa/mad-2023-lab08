package com.example.lab8.related_to_data

import retrofit2.http.GET

interface RetrofitController {
    @GET("api.json")
    suspend fun getCards(): List<Card>
}
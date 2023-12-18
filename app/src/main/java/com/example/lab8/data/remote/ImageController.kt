package com.example.lab8.data.remote

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ImageController {

    @Streaming
    @GET
    suspend fun getImage(@Url fileName: String): ResponseBody

    companion object {

        fun getInstance(baseUrl: String): ImageController =
            Retrofit.Builder().baseUrl(baseUrl).build().create(ImageController::class.java)
    }
}

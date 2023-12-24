package com.example.myapplication.data.client

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ImageClient {

    @Streaming
    @GET
    suspend fun getImage(@Url fileName: String): ResponseBody

    companion object {

        fun getInstance(baseUrl: String): ImageClient =
            Retrofit.Builder().baseUrl(baseUrl).build().create(ImageClient::class.java)
    }
}
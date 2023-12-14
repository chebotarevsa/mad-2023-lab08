package com.example.myapplication.data.remote

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ImgController {
    @Streaming
    @GET
    suspend fun getImage(@Url fileName: String): ResponseBody

    companion object {
        fun getInstance(baseUrl: String): ImgController =
            Retrofit.Builder().baseUrl(baseUrl).build().create(ImgController::class.java)
    }
}
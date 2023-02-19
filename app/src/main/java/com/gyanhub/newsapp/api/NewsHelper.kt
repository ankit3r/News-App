package com.gyanhub.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsHelper {
    private const val baseUrl = "https://newsapi.org/v2/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
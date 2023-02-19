package com.gyanhub.newsapp.api

import com.gyanhub.newsapp.models.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApi {
    @GET
   suspend fun getLatestNews(@Url url: String):Response<NewsModel>
}

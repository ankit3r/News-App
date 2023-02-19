package com.gyanhub.newsapp.newsApplication

import android.app.Application
import com.gyanhub.newsapp.api.NewsApi
import com.gyanhub.newsapp.api.NewsHelper
import com.gyanhub.newsapp.repository.NewsRepository

class NewsApplication :Application() {
    lateinit var newsRepository: NewsRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val newsApi = NewsHelper.getInstance().create(NewsApi::class.java)
        newsRepository = NewsRepository(newsApi)
    }
}
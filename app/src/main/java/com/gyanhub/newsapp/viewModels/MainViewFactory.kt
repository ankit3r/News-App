package com.gyanhub.newsapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gyanhub.newsapp.repository.NewsRepository

class MainViewFactory (private val newsRepository: NewsRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(newsRepository) as T
        }
    }

package com.gyanhub.newsapp.utils

sealed class NetworkResult <T>(val data: T? = null, val massage : String? = null){
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(massage: String?) : NetworkResult<T>(null,massage)
    class Loading<T> : NetworkResult<T>()
}
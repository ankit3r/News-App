package com.gyanhub.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gyanhub.newsapp.api.NewsApi
import com.gyanhub.newsapp.models.NewsModel
import com.gyanhub.newsapp.utils.NetworkResult


class NewsRepository(
    private val newsApi:NewsApi
) {
    private  val newsLiveDatas=MutableLiveData<NetworkResult<NewsModel>>()
     val data : LiveData<NetworkResult<NewsModel>>
         get() = newsLiveDatas


    suspend fun getNews(url:String){
       try{
           newsLiveDatas.postValue(NetworkResult.Loading())
           val newsResult = newsApi.getLatestNews(url)
           if(newsResult.isSuccessful && newsResult.body() != null){
               newsLiveDatas.postValue(NetworkResult.Success(newsResult.body()!!))
           }else if (newsResult.errorBody() !=null){
               newsLiveDatas.postValue(NetworkResult.Error("Something went wrong"))
           }else{
               newsLiveDatas.postValue(NetworkResult.Error("Something went wrong"))
           }
       }catch (e: Exception){
           newsLiveDatas.postValue(NetworkResult.Error("Network to Slow $e"))
       }
    }

}
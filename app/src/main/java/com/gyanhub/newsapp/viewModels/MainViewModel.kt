package com.gyanhub.newsapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gyanhub.newsapp.models.NewsModel
import com.gyanhub.newsapp.repository.NewsRepository
import com.gyanhub.newsapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val newsRepository: NewsRepository) : ViewModel(){
        val data : LiveData<NetworkResult<NewsModel>>
            get() = newsRepository.data

    private  val catLiveDatas= MutableLiveData<Array<String>>()
    private val category = arrayOf("Technology","Science","Sports","General","Business","Health","Entertainment")
    val categoryLiveData : LiveData<Array<String>>
        get() = catLiveDatas


    private  val shortLiveDatas= MutableLiveData<Array<String>>()
    private val short = arrayOf("popularity","publishedAt","relevancy")
    val shortByLiveData : LiveData<Array<String>>
        get() = shortLiveDatas

    private  val shortCuntLiveDatas= MutableLiveData<Array<String>>()
    private val shortCount = arrayOf("in","us","ae", "ar","at","au","be","bg","br","ca","ch","cn","co","cu","cz","de","eg",
        "fr","gb","gr","hk","hu","id","ie","il","it","jp","kr","lt","lv","ma","mx","my","ng","nl"
        ,"no","nz","ph","pl","pt","ro","rs","ru","sa","se","sg","si","sk","th","tr","tw","ua","ve","za")
    val shortByCountLiveData : LiveData<Array<String>>
        get() = shortCuntLiveDatas


    fun getNews(url:String){
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getNews(url)
        }
    }
    init {
        catLiveDatas.postValue(category)
        shortLiveDatas.postValue(short)
        shortCuntLiveDatas.postValue(shortCount)
    }

}
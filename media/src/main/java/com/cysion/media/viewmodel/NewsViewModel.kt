package com.cysion.media.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.media.entity.NewsInfoEntity
import com.cysion.media.net.MediaCaller

class NewsViewModel:BaseViewModel() {

    val mLiveNews = MutableLiveData<ArrayList<NewsInfoEntity>>()

    fun getNewsList() {
        launchWithFilter(
                taskOfRetrofit = {
                    MediaCaller.api.getNewList()
                },
                onSuccess = {
                    mLiveNews.postValue(it)
                },
                showLoading = true
        )
    }
}
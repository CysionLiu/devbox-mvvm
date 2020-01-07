package com.cysion.usercenter.viewmodels

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.entity.CollectEntity
import com.cysion.usercenter.net.UserCaller

class CollectPageViewModel:BaseViewModel() {


    val mLiveCollectList: MutableLiveData<MutableList<CollectEntity>> = MutableLiveData()
    val mLiveCancleState: MutableLiveData<Boolean> = MutableLiveData()

    //获得收藏列表
    fun getBlogs(page: Int = 1) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.getCollectList()
                },
                onSuccess = {
                    mLiveCollectList.value = it
                },
                showLoading = true
        )
    }

    //取消收藏
    fun delCol(itemId: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.unCollectBlog(itemId)
                },
                onSuccess = {
                    mLiveCancleState.value = true
                },
                showLoading = true
        )
    }
}
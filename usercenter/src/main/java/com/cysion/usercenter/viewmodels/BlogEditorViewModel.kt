package com.cysion.usercenter.viewmodels

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.net.UserCaller

class BlogEditorViewModel:BaseViewModel() {
    val mLiveCreateState: MutableLiveData<Boolean> = MutableLiveData()
    val mLiveUpdateState: MutableLiveData<Boolean> = MutableLiveData()

    fun createBlog(title: String, text: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.createBlog(title, text)
                },
                onSuccess = {
                    mLiveCreateState.value = true
                },
                showLoading = true
        )
    }

    fun updateBlog(title: String, text: String, blogId: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.updateBlog(title, text, blogId)
                },
                onSuccess = {
                    mLiveUpdateState.value = true
                },
                showLoading = true
        )
    }
}
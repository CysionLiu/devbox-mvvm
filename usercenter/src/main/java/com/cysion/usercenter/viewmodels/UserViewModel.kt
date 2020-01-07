package com.cysion.usercenter.viewmodels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.entity.UserEntity
import com.cysion.usercenter.helper.UserCache
import com.cysion.usercenter.net.UserCaller
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {

    val mLiveUserInfo = MutableLiveData<UserEntity>()

    fun login(username: String, pwd: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.login(username,pwd)
                },
                onSuccess = {
                    UserCache.save(it)
                    mLiveUserInfo.value=it
                },
                showLoading = true
        )
    }
    fun updateUserInfo(nickName: String, desc: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.updateUserInfo(nickName,desc)
                },
                onSuccess = {
                    UserCache.save(it)
                    mLiveUserInfo.value=it
                },
                showLoading = true
        )
    }

    fun getUserInfo() {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.getUserInfo()
                },
                onSuccess = {
                    if (!TextUtils.isEmpty(it.token)) {
                        UserCache.save(it)
                    }
                }
        )
    }
    fun syncUserInfo() {
        viewModelScope.launch{
            UserCache.fromCache()
        }
    }
}
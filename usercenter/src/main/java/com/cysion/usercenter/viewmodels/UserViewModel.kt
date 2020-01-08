package com.cysion.usercenter.viewmodels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.constant.LOGIN_IN
import com.cysion.usercenter.entity.DetailUserEntity
import com.cysion.usercenter.entity.UserEntity
import com.cysion.usercenter.event.UserEvent
import com.cysion.usercenter.helper.UserCache
import com.cysion.usercenter.net.UserCaller
import org.greenrobot.eventbus.EventBus

class UserViewModel : BaseViewModel() {

    val mLiveUserInfo = MutableLiveData<UserEntity>()
    val mLiveRegisterState = MutableLiveData<Boolean>()
    val mLiveDetailInfo = MutableLiveData<DetailUserEntity>()

    fun login(username: String, pwd: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.login(username, pwd)
                },
                onSuccess = {
                    UserCache.save(it)
                    mLiveUserInfo.value = it
                },
                showLoading = true
        )
    }

    fun updateUserInfo(nickName: String, desc: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.updateUserInfo(nickName, desc)
                },
                onSuccess = {
                    UserCache.save(it)
                    mLiveUserInfo.value = it
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

    fun register(username: String,
                 password: String,
                 password2: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.register(username, password, password2)
                },
                onSuccess = {
                    UserCache.save(it)
                    EventBus.getDefault().post(UserEvent(LOGIN_IN, ""))
                    mLiveRegisterState.postValue(true)
                },
                showLoading = true
        )
    }
    fun getPeopleInfo(map:Map<String,String>) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.getPeopleInfo(map)
                },
                onSuccess = {
                    mLiveDetailInfo.postValue(it)
                },
                showLoading = true
        )
    }

}
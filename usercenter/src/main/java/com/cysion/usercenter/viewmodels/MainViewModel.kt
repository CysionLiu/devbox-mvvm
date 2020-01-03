package com.cysion.usercenter.viewmodels

import androidx.lifecycle.viewModelScope
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.helper.UserCache
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    fun syncUserInfo() {
        viewModelScope.launch{
            UserCache.fromCache()
        }
    }
}
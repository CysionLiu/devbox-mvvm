package com.cysion.usercenter.viewmodels

import androidx.lifecycle.viewModelScope
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.ktbox.net.ErrorHandler
import com.cysion.usercenter.entity.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @param taskOfRetrofit 网络请求，即在接口里声明的网络请求方法
 * @param onSuccess 请求结果，目标数据的回调
 * @param onError 请求未得到目标数据的回调
 * @param showLoading 是否展示加载框
 * */

fun <T> BaseViewModel.launchWithFilter(taskOfRetrofit: suspend CoroutineScope.() -> ApiResult<T>,
                                       onSuccess:suspend (data:T)->Unit,
                                       onError: (code: Int, msg: String) -> Unit = { code, msg ->
                                           postError(code,msg)
                                       },
                                       showLoading: Boolean = false) {

    if (showLoading) {
        startLoading()
    }
    viewModelScope.launch {
        try {
            val ans = withContext(Dispatchers.IO) {
                taskOfRetrofit()
            }
            if (ans.isSuccessful()) {
                onSuccess(ans.data)
            } else {
                onError(ans.code, ans.msg)
            }
        } catch (e: java.lang.Exception) {
            val handle = ErrorHandler.handle(e)
            onError(handle.errorCode, handle.errorMsg)
        } finally {
            if (showLoading) {
                stopLoading()
            }
        }
    }
}

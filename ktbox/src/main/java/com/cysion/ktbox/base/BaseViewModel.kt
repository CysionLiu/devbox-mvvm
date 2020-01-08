package com.cysion.ktbox.base

import androidx.lifecycle.*
import com.cysion.ktbox.bean.StateEvent
import com.cysion.ktbox.net.ErrorHandler
import com.cysion.ktbox.utils.logd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val state = MutableLiveData<StateEvent>()

    internal fun observeState(lifecycleOwner: LifecycleOwner, observer: Observer<StateEvent>) {
        state.observe(lifecycleOwner, observer)
    }

    protected fun postState(aType: Int, aMsg: String) {
        logd("flag--postState:type:$aType,msg:$aMsg ")
        val value = state.value ?: StateEvent(0, "")
        value.apply {
            type = aType
            msg = aMsg
        }
        state.value = value
    }

    protected fun refreshOk(msg: String = "") {
        postState(StateEvent.REFRESH_OK, msg)
    }

    protected fun refreshFail(msg: String = "") {
        postState(StateEvent.REFRESH_FAIL, msg)
    }

    protected fun loadMoreOk(msg: String = "") {
        postState(StateEvent.LOAD_MORE_OK, msg)
    }

    protected fun loadMoreFail(msg: String = "") {
        postState(StateEvent.LOAD_MORE_FAIL, msg)
    }

    fun startLoading(msg: String = "") {
        postState(StateEvent.LOADING, msg)
    }

    fun stopLoading() {
        postState(StateEvent.LOADED, "")
    }

    /*发送错误信息，在Ui中的onStateEventChanged()方法接收*/
    fun postError(type: Int, msg: String) {
        postState(type, msg)
    }

    /*
    * 处理retrofit网络请求，得到原始对象数据
    * @blockUiSafe，同步代码块，所以挂起任务要求主线程安全*/
    fun <T> launchRawData(showLoading: Boolean = false,
                          onError: (code: Int, msg: String) -> Unit = { code, msg ->
                              postError(code, msg)
                          },
                          blockUiSafe: suspend CoroutineScope.() -> T) {
        if (showLoading) {
            this.startLoading()
        }
        viewModelScope.launch {
            try {
                blockUiSafe()
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
}
/*当用不到viewmodel时，可用这个*/
class NoViewModel:BaseViewModel()
package com.cysion.ktbox.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.cysion.ktbox.bean.StateEvent
import retrofit2.Call

abstract class BaseViewModel : ViewModel() {

    private val state = MutableLiveData<StateEvent>()
    private val calls:MutableList<Call<*>> = mutableListOf()


    internal fun observeState(lifecycleOwner: LifecycleOwner, observer: Observer<StateEvent>) {
        state.observe(lifecycleOwner, observer)
    }

    protected fun postState(aType: Int, aMsg: String) {
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

    protected fun startLoading(msg: String = "") {
        postState(StateEvent.LOADING, msg)
    }

    protected fun stopLoading() {
        postState(StateEvent.LOADED, "")
    }

    protected fun error(type:Int,msg:String){
        postState(type, msg)
    }

    fun Call<*>.addTo(){
        calls.add(this)
    }
}
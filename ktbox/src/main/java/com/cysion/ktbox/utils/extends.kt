package com.cysion.ktbox.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class UIViewModel: ViewModel()
/*在主线程启动一个协程，执行需要在主线程进行的可能的耗时任务(比如延时操作)
* 当activity销毁时，协程也会自动取消，viewmodel也会自动清理*/
fun AppCompatActivity.launchUI(block: suspend CoroutineScope.() -> Unit){
    val get = ViewModelProviders.of(this).get(UIViewModel::class.java)
    get.viewModelScope.launch {
        block()
    }
}

/*在主线程启动一个协程，执行需要在主线程进行的可能的耗时任务(比如延时操作)
* 当fragment销毁时，协程也会自动取消，viewmodel也会自动清理*/
fun Fragment.launchUI(block: suspend CoroutineScope.() -> Unit){
    val get = ViewModelProviders.of(this).get(UIViewModel::class.java)
    get.viewModelScope.launch {
        block()
    }
}


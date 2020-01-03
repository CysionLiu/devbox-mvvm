package com.cysion.ktbox.listener

/*
* 刷新，上拉加载更多的事件监听器*/
interface IRefreshListener {

    fun onRefreshOk(msg: String?)

    fun onRefreshFail(msg: String?)

    fun onLoadMoreOk(msg: String?)

    fun onLoadMoreFail(msg: String?)

}
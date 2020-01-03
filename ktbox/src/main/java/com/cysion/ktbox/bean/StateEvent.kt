package com.cysion.ktbox.bean

class StateEvent(var type: Int, var msg: String?){
    companion object{
        val LOADING = 1000
        val LOADED = 1001
        val REFRESH_OK = 1002
        val REFRESH_FAIL = 1003
        val LOAD_MORE_OK = 1004
        val LOAD_MORE_FAIL = 1005
    }
}

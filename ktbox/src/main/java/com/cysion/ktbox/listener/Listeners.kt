package com.cysion.ktbox.listener

interface PureListener<T>{
    fun done(data:T)
    fun error(code:Int,msg:String)
}
package com.cysion.ktbox.listener

interface PureListener<T>{
    fun done(data:T)
    fun error(code:Int,msg:String)
}
typealias CallBack<T> = (t:T)->Unit
typealias CallBack2<T,R> = (t:T,r:R)->Unit
typealias CallBack3<T,R,P> = (t:T,r:R,p:P)->Unit
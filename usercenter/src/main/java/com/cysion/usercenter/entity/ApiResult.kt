package com.cysion.usercenter.entity

class ApiResult<T>(val code: Int, val msg: String, val data: T) {
    fun isSuccessful(): Boolean {
        return code == 200
    }
}
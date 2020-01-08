package com.cysion.media.net

import com.cysion.ktbox.net.ApiException

/**
 * 数据基类；若字段不一致，可仿照再写一个
 */
data class BaseResp<T>(val code: Int, val msg: String, val result: T) {
    fun isSuccessful(): Boolean {
        return code == 200
    }
}


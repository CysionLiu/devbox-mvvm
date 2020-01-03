package com.cysion.usercenter.viewmodels

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.ktbox.net.ApiException
import com.cysion.ktbox.net.ErrorHandler
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.entity.Carousel
import com.cysion.usercenter.net.UserCallBack
import com.cysion.usercenter.net.UserCaller
import com.cysion.usercenter.net.call
import com.orhanobut.logger.Logger
import okhttp3.Request

class SqureViewModel : BaseViewModel() {

    val mLiveCarousel: MutableLiveData<MutableList<Carousel>> = MutableLiveData<MutableList<Carousel>>().apply {
        value = mutableListOf()
    }
    val mLiveBlogs: MutableLiveData<MutableList<Blog>> = MutableLiveData<MutableList<Blog>>().apply {
        value = mutableListOf()
    }
    val mLivePride: MutableLiveData<Int> = MutableLiveData<Int>().apply {
        value = 0
    }

    override fun onCleared() {
        super.onCleared()
        mLivePride.value = null
        mLiveBlogs.value?.clear()
        mLiveCarousel.value?.clear()
    }

    //获得顶部轮播
    fun getCarousel() {
        Logger.d("flag--getCarousel:40： ")
        UserCaller.api.getCarousel()
                .call {
                    onSuccess { request, obj ->
                        Logger.d("flag--getCarousel:43： ")
                        mLiveCarousel.value!!.clear()
                        mLiveCarousel.value!!.addAll(obj)
                        mLiveCarousel.postValue(mLiveCarousel.value)
                    }
                    onError { request, error ->
                        error(error.errorCode, error.errorMsg)
                    }
                }
    }

    //获得博客列表
    fun getBlogs(pageNum: Int) {
        UserCaller.api.getBlogList(page = pageNum)
                .call {
                    onError { request, error ->
                        if (pageNum == 1) {
                            refreshFail()
                        } else {
                            loadMoreFail()
                        }
                        val handle = ErrorHandler.handle(error)
                        postState(handle.errorCode, handle.errorMsg)
                    }
                    onSuccess { request, obj ->
                        if (pageNum == 1) {
                            mLiveBlogs.value!!.clear()
                            refreshOk()
                        } else {
                            loadMoreOk()
                        }
                        mLiveBlogs.value!!.addAll(obj)
                        mLiveBlogs.postValue(mLiveBlogs.value)
                    }
                }
    }


    //点赞博客
    fun pride(blog: Blog, pos: Int) {
        startLoading()
        UserCaller.api.prideBlog(blog.blogId)
                .call {
                    onSuccess { request, obj ->
                        stopLoading()
                        blog.isPrided = 1
                        blog.prideCount++
                        mLivePride.value = pos

                    }
                    onError { request, error ->
                        stopLoading()
                        postState(error.errorCode, error.errorMsg)
                    }
                }
    }

    //取消点赞博客
    fun unPride(blog: Blog, pos: Int) {
        startLoading()
        UserCaller.api.unPrideBlog(blog.blogId)
                .call(object : UserCallBack<Any?> {
                    override fun onSuccess(request: Request, obj: Any?) {
                        stopLoading()
                        blog.isPrided = 0
                        blog.prideCount--
                        mLivePride.value = pos
                    }

                    override fun onError(request: Request, error: ApiException) {
                        stopLoading()
                        postState(error.errorCode, error.errorMsg)
                    }
                })
    }
}
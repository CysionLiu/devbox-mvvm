package com.cysion.usercenter.viewmodels

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.entity.Carousel
import com.cysion.usercenter.net.UserCaller

class SqureViewModel : BaseViewModel() {

    val mLiveCarousel: MutableLiveData<MutableList<Carousel>> = MutableLiveData<MutableList<Carousel>>()
            .apply {
                value = mutableListOf()
            }
    val mLiveBlogs: MutableLiveData<MutableList<Blog>> = MutableLiveData<MutableList<Blog>>()
            .apply {
                value = mutableListOf()
            }
    val mLivePride: MutableLiveData<Int> = MutableLiveData<Int>()
            .apply {
                value = 0
            }

    //获得顶部轮播
    fun getCarousel() {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.getCarousel()
                },
                onSuccess = {
                    mLiveCarousel.value!!.clear()
                    mLiveCarousel.value!!.addAll(it)
                    mLiveCarousel.postValue(mLiveCarousel.value)
                }
        )
    }

    //获得博客列表
    fun getBlogs(pageNum: Int) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.getBlogList(page = pageNum)
                },
                onSuccess = {
                    if (pageNum == 1) {
                        mLiveBlogs.value!!.clear()
                        refreshOk()
                    } else {
                        loadMoreOk()
                    }
                    mLiveBlogs.value!!.addAll(it)
                    mLiveBlogs.postValue(mLiveBlogs.value)
                },
                onError = { code, msg ->
                    if (pageNum == 1) {
                        refreshFail()
                    } else {
                        loadMoreFail()
                    }
                    postError(code, msg)
                }
        )
    }

    //点赞博客
    fun pride(blog: Blog, pos: Int) {
        launchRawData {
            UserCaller.api.prideBlog(blog.blogId)
            blog.isPrided = 1
            blog.prideCount++
            mLivePride.value = pos
        }
    }

    //取消点赞博客
    fun unPride(blog: Blog, pos: Int) {
        launchRawData {
            UserCaller.api.unPrideBlog(blog.blogId)
            blog.isPrided = 0
            blog.prideCount--
            mLivePride.value = pos
        }
    }
}
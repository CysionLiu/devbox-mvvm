package com.cysion.usercenter.viewmodels

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.net.UserCaller

class UserBlogViewModel:BaseViewModel() {
    val mLiveDelState: MutableLiveData<Boolean> = MutableLiveData()
    val mLivePrideState: MutableLiveData<Int> = MutableLiveData()
    val mLiveBlogs: MutableLiveData<MutableList<Blog>> = MutableLiveData<MutableList<Blog>>()

    fun getBlogs(page: Int = 1) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.getUserBlogList(page)
                },
                onSuccess = {
                    mLiveBlogs.postValue(it)
                },
                showLoading = true
        )
    }

    fun deleteBlog(blogId: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.delBlog(blogId)
                },
                onSuccess = {
                    mLiveDelState.postValue(true)
                },
                showLoading = true
        )
    }

    fun pride(blog: Blog, pos: Int) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.prideBlog(blog.blogId)
                },
                onSuccess = {
                    blog.isPrided = 1
                    blog.prideCount++
                    mLivePrideState.value = pos
                }
        )
    }

    fun unPride(blog: Blog, pos: Int) {
        launchWithFilter(
                {
                    UserCaller.api.unPrideBlog(blog.blogId)
                },
                {
                    blog.isPrided = 0
                    blog.prideCount--
                    mLivePrideState.value = pos
                }
        )
    }
}
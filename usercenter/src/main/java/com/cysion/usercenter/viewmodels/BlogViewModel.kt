package com.cysion.usercenter.viewmodels

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.entity.CommentEntity
import com.cysion.usercenter.net.UserCaller

class BlogViewModel:BaseViewModel() {

    val mLivePrideState: MutableLiveData<Boolean> = MutableLiveData()
    val mLiveCollectState: MutableLiveData<Boolean> = MutableLiveData()
    val mLiveCommentState: MutableLiveData<Boolean> = MutableLiveData()
    val mLiveCommentList: MutableLiveData<MutableList<CommentEntity>> = MutableLiveData()
    val mLiveBlog: MutableLiveData<Blog> = MutableLiveData()



    //点赞博客
    fun pride(blog: Blog) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.prideBlog(blog.blogId)
                },
                onSuccess = {
                    blog.isPrided = 1
                    blog.prideCount++
                    mLivePrideState.value = true
                }
        )
    }

    //取消点赞博客
    fun unPride(blog: Blog) {
        launchWithFilter(
                {
                    UserCaller.api.unPrideBlog(blog.blogId)
                },
                {
                    blog.isPrided = 0
                    blog.prideCount--
                    mLivePrideState.value = false
                }
        )
    }


    fun collect(blogid: String) {
        launchWithFilter(
                taskOfRetrofit = {
                    UserCaller.api.collectBlog(blogid)
                },
                onSuccess = {
                    mLiveCollectState.value = true
                }
        )
    }

    fun unCollect(blogid: String) {
        launchWithFilter(
                {
                    UserCaller.api.unCollectBlog(blogid)
                },
                {
                    mLiveCollectState.value = false
                }
        )
    }

    fun comment(blogId: String, content: String) {
        launchWithFilter(
                {
                    UserCaller.api.commentBlog(blogId,content)
                },
                {
                    mLiveCommentState.value = false
                },
                showLoading = true
        )
    }

    //获取该博客的评论
    fun getComments(blogId: String) {
        launchWithFilter(
                {
                    UserCaller.api.getComments(blogId)
                },
                {
                    mLiveCommentList.value = it
                },
                showLoading = true
        )
    }


    fun getBlog(blogId: String) {
        launchWithFilter(
                {
                    UserCaller.api.getBlog(blogId)
                },
                {
                    mLiveBlog.value = it
                },
                showLoading = true
        )
    }
}
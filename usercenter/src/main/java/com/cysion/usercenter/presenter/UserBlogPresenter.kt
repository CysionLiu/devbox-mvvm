package com.cysion.usercenter.presenter

import com.cysion.ktbox.base.BasePresenter
import com.cysion.ktbox.net.ApiException
import com.cysion.ktbox.net.BaseResponseRx
import com.cysion.other.addTo
import com.cysion.targetfun._subscribe
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.net.UserCallBack
import com.cysion.usercenter.net.UserCaller
import com.cysion.usercenter.net.call
import com.cysion.usercenter.ui.iview.UserBlogListView
import okhttp3.Request

class UserBlogPresenter : BasePresenter<UserBlogListView>() {

    fun getBlogs(page: Int = 1) {
        checkViewAttached()
        attchedView?.loading()
        UserCaller.api.getUserBlogList(page)
            .compose(BaseResponseRx.validateToMain())
            ._subscribe {
                _onNext {
                    attchedView?.apply {
                        stopLoad()
                        setList(it)
                    }
                }
                _onError {
                    attchedView?.stopLoad()
                    error(it)
                }
            }.addTo(compositeDisposable)

    }

    fun deleteBlog(blogId: String) {
        checkViewAttached()
        attchedView?.loading()
        UserCaller.api.delBlog(blogId)
            .compose(BaseResponseRx.validateToMain())
            ._subscribe {
                _onNext {
                    attchedView?.apply {
                        stopLoad()
                        delSuccessful()
                    }
                }
                _onError {
                    attchedView?.stopLoad()
                    error(it)
                }
            }.addTo(compositeDisposable)
    }

    fun pride(blog: Blog, pos: Int) {
        checkViewAttached()
        attchedView?.loading()
        UserCaller.api.prideBlog(blog.blogId)
                .call(object : UserCallBack<Any?> {
                    override fun onSuccess(request: Request, obj: Any?) {
                        attchedView?.apply {
                            stopLoad()
                            blog.isPrided = 1
                            blog.prideCount++
                            prideOk(pos)
                        }
                    }

                    override fun onError(request: Request, error: ApiException) {
                        attchedView?.stopLoad()
                        error(error)
                    }
                })
    }

    fun unPride(blog: Blog, pos: Int) {
        checkViewAttached()
        attchedView?.loading()
        UserCaller.api.unPrideBlog(blog.blogId)
                .call(object : UserCallBack<Any?> {
                    override fun onSuccess(request: Request, obj: Any?) {
                        attchedView?.apply {
                            stopLoad()
                            blog.isPrided = 0
                            blog.prideCount--
                            unprideOk(pos)
                        }
                    }

                    override fun onError(request: Request, error: ApiException) {
                        attchedView?.stopLoad()
                        error(error)
                    }
                })
    }

}
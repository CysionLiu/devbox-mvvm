package com.cysion.usercenter.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.color
import com.cysion.other.gotoActivity
import com.cysion.targetfun.withTextChangedListener
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.constant.*
import com.cysion.usercenter.event.BlogEvent
import com.cysion.usercenter.viewmodels.BlogEditorViewModel
import kotlinx.android.synthetic.main.activity_blog_editor.*
import org.greenrobot.eventbus.EventBus

class BlogEditorActivity : BaseModelActivity<BlogEditorViewModel>(){

    private val extra by lazy {
        intent.getBundleExtra(BUNDLE_KEY)?:Bundle()
    }

    private val title: String by lazy {
        extra.getString(BLOG_TITLE)?:""
    }
    private val content: String by lazy {
        extra.getString(BLOG_CONTENT)?:""
    }
    private val blogId: String by lazy {
        extra.getString(BLOG_ID)?:""
    }
    private val type: Int by lazy {
        extra.getInt(BLOG_EDIT_TYPE)
    }

    override fun getLayoutId(): Int = R.layout.activity_blog_editor

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        initTextWatcher()
        initTopBar()
        initEditor()
    }
    override fun observeModel() {
        viewModel.mLiveCreateState.observe(this, Observer {
            if(it){
                toast("创建成功")
                sendBusEvent(CREATE_BLOG, "")
                finish()
            }
        })
        viewModel.mLiveUpdateState.observe(this, Observer {
            if(it){
                toast("更新成功")
                sendBusEvent(UPDATE_BLOG, blogId)
            }
        })
    }

    private fun initTextWatcher() {
        etTitle.withTextChangedListener {
            ifAfterTextChanged {
                it?.apply {
                    if (length >= 139) {
                        toast("超过140字了")
                    }
                }
            }
        }
        etContent.withTextChangedListener {
            ifAfterTextChanged {
                it?.apply {
                    if (length >= 2000) {
                        toast("超过最大字数了")
                    }
                }
            }
        }
    }

    private fun initEditor() {
        if (type != 0) {
            etTitle.setText(title)
            etContent.setText(content)
        }
    }

    private fun initTopBar() {
        topbar.apply {
            initElements(right = TopBar.ELEMENT.TEXT)
            setTitle("编辑")
            setTexts(if (type == 0) "发布" else "更新", TopBar.Pos.RIGHT)
        }.setOnTopBarClickListener { _, pos ->
            if (pos == TopBar.Pos.LEFT) {
                finish()
            } else if (pos == TopBar.Pos.RIGHT) {
                if (etTitle.text.isEmpty()) {
                    toast("请输入标题")
                } else {
                    if (type == 0) {
                        viewModel.createBlog(
                            etTitle.text.toString().trim()
                            , etContent.text.toString().trim()
                        )
                    } else {
                        viewModel.updateBlog(
                            etTitle.text.toString().trim()
                            , etContent.text.toString().trim(),
                            blogId
                        )

                    }

                }
            }
        }
    }


    override fun onReceivedStateEvent(type: Int, msg: String) {
        toast(msg)
    }

    //发送eventbus事件，用于更新首页列表
    private fun sendBusEvent(tag: Int, msg: String) {
        EventBus.getDefault().post(BlogEvent(tag, msg))
    }

    /*
    启动该activity
    type=0，创建；1，编辑
     */
    companion object {
        fun start(activity: Activity, title: String, content: String, type: Int = 0, blogId: String = "") {
            val b = Bundle()
            b.putString(BLOG_TITLE, title)
            b.putString(BLOG_CONTENT, content)
            b.putInt(BLOG_EDIT_TYPE, type)
            b.putString(BLOG_ID, blogId)
            activity.gotoActivity<BlogEditorActivity>(BUNDLE_KEY, b)
        }
    }
}

package com.cysion.usercenter.ui.activity

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.base.ITEM_CLICK
import com.cysion.ktbox.net.ErrorStatus
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.color
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.adapter.BlogAdapter
import com.cysion.usercenter.adapter.BlogAdapter.Companion.DEL
import com.cysion.usercenter.adapter.BlogAdapter.Companion.EDIT
import com.cysion.usercenter.adapter.BlogAdapter.Companion.PRIDE
import com.cysion.usercenter.adapter.BlogAdapter.Companion.USER_PAGE
import com.cysion.usercenter.constant.*
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.event.BlogEvent
import com.cysion.usercenter.helper.BlogHelper
import com.cysion.usercenter.viewmodels.UserBlogViewModel
import com.cysion.wedialog.WeDialog
import kotlinx.android.synthetic.main.activity_user_blogs.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UserBlogActivity : BaseModelActivity<UserBlogViewModel>() {

    private lateinit var blogAdapter: BlogAdapter
    private val mBlogs: MutableList<Blog> = mutableListOf()

    override fun getLayoutId(): Int = R.layout.activity_user_blogs

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        topbar.apply {
            setTitle("我的博客")
            setOnTopBarClickListener { obj, pos ->
                if (pos == TopBar.Pos.LEFT) {
                    finish()
                }
            }
        }
        initRV()
    }

    private fun initRV() {
        rvBloglist.layoutManager = LinearLayoutManager(self)
        blogAdapter = BlogAdapter(mBlogs, self, USER_PAGE)
        rvBloglist.adapter = blogAdapter
        blogAdapter.setOnTypeClickListener { obj, position, flag ->
            if (flag == ITEM_CLICK) {
                BlogDetailActivity.start(self, obj)
            } else if (flag == PRIDE) {
                if (obj.isPrided == 1) {
                    viewModel.unPride(obj,position)
                } else {
                    viewModel.pride(obj,position)
                }
            } else if (flag == EDIT) {
                BlogEditorActivity.start(self, obj.title, obj.text, 1, obj.blogId)
            } else if (flag == DEL) {
                WeDialog.normal(self)
                        .setTitle("提示")
                        .setMsg("确认删除这个博客吗？")
                        .show {
                            it.dismissAllowingStateLoss()
                            viewModel.deleteBlog(obj.blogId)
                        }
            }
        }
    }
    override fun observeModel() {
        viewModel.mLivePrideState.observe(this, Observer {
            blogAdapter.notifyItemChanged(it)
        })
        viewModel.mLiveDelState.observe(this, Observer {
            toast("删除博客成功")
            viewModel.getBlogs()
        })
        viewModel.mLiveBlogs.observe(this, Observer {
            mBlogs.clear()
            mBlogs.addAll(it)
            blogAdapter.notifyDataSetChanged()
            if (mBlogs.size == 0) {
                multiView.showEmpty()
            }
        })
    }

    override fun initData() {
        super.initData()
        viewModel.getBlogs()
    }

    override fun onReceivedStateEvent(type: Int, msg: String) {
        toast(msg)
        if (mBlogs.size == 0) {
            multiView.showEmpty()
            if (type == ErrorStatus.NETWORK_ERROR) {
                multiView.showNoNetwork()
            }
        }
    }

    //接收BlogEvent事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receive(event: BlogEvent) {
        when (event.tag) {
            PRIDE_OK ->
                BlogHelper.getBlog(event.msg, mBlogs)?.apply {
                    isPrided = 1
                    prideCount++
                }
            PRIDE_CANCEL ->
                BlogHelper.getBlog(event.msg, mBlogs)?.apply {
                    isPrided = 0
                    prideCount--
                }
            COLLECT_OK ->
                BlogHelper.getBlog(event.msg, mBlogs)?.isCollected = 1
            COLLECT_CANCEL ->
                BlogHelper.getBlog(event.msg, mBlogs)?.isCollected = 0
            CREATE_BLOG, UPDATE_BLOG, COMMENT ->
                viewModel.getBlogs()
        }
        blogAdapter.notifyDataSetChanged()
    }
}

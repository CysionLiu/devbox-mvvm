package com.cysion.usercenter.ui.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.clickWithLimit
import com.cysion.other.color
import com.cysion.other.gotoActivity
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.adapter.CommentAdapter
import com.cysion.usercenter.constant.*
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.entity.CommentEntity
import com.cysion.usercenter.event.BlogEvent
import com.cysion.usercenter.helper.BlogHelper
import com.cysion.usercenter.viewmodels.BlogViewModel
import kotlinx.android.synthetic.main.activity_blog_detail.*
import org.greenrobot.eventbus.EventBus

class BlogDetailActivity : BaseModelActivity<BlogViewModel>() {

    //评论相关
    private val commentList = mutableListOf<CommentEntity>()
    private lateinit var commentAdapter: CommentAdapter
    private var blog: Blog? = null
    private var mBlogId = ""

    override fun getLayoutId(): Int = R.layout.activity_blog_detail

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        topbar.apply {
            setTitle("详情")
            setOnTopBarClickListener { obj, pos ->
                if (pos == TopBar.Pos.LEFT) {
                    finish()
                }
            }
        }
        llAuthorArea.setOnClickListener {
            PeopleInfoActivity.start(self, blog?.authorId!!)
        }
        initCommentView()
    }

    override fun observeModel() {
        viewModel.mLiveBlog.observe(this, Observer {
            blog = it
            fillView()
        })
        viewModel.mLivePrideState.observe(this, Observer {
            blog?.run {
                sendBusEvent(if (it) PRIDE_OK else PRIDE_CANCEL, blogId)
            }
            fillView()

        })
        viewModel.mLiveCollectState.observe(this, Observer {
            blog?.run {
                isCollected = if (it) 1 else 0
                sendBusEvent(if (it) COLLECT_OK else COLLECT_CANCEL, blogId)
                fillView()
            }
        })
        viewModel.mLiveCommentState.observe(this, Observer {
            blog?.run {
                toast("评论成功")
                viewModel.getComments(blogId)
                sendBusEvent(COMMENT, blogId)
            }
        })
        viewModel.mLiveCommentList.observe(this, Observer {
            commentList.clear()
            commentList.addAll(it)
            commentAdapter.notifyDataSetChanged()
            if (commentList.size == 0) {
                multiView.showEmpty()
            } else {
                multiView.showContent()
            }
        })

    }

    //初始化评论列表
    private fun initCommentView() {
        rvCommentlist.isNestedScrollingEnabled = false
        rvCommentlist.layoutManager = LinearLayoutManager(self)
        commentAdapter = CommentAdapter(commentList, self)
        rvCommentlist.adapter = commentAdapter
    }

    override fun initData() {
        super.initData()
        val bundleExtra = intent.getBundleExtra(BUNDLE_KEY)
        bundleExtra?.run {
            val tmp = bundleExtra.getSerializable(BLOG)
            if (tmp != null) {
                blog = tmp as Blog
            }
            mBlogId = bundleExtra.getString(BLOG_ID) ?: ""
        }
        //若传来空id，则blog必然不能为空
        if (TextUtils.isEmpty(mBlogId)) {
            mBlogId = blog?.blogId!!
        } else {
            viewModel.getBlog(mBlogId)
        }
        fillView()
        initEvent()
        viewModel.getComments(mBlogId)
    }

    private fun fillView() {
        blog?.apply {
            tvBlogTitle.text = title
            tvContent.text = text
            tvAuthorName.text = authorName

            Glide.with(self).load(authorAvatar)
                    .apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.place_holder))
                    .into(ivAvatar)
            Glide.with(self).load(icon)
                    .apply(RequestOptions.placeholderOf(R.mipmap.place_holder_big))
                    .into(ivIcon)
            tvPride.text = "$prideCount"
            ivPride.isSelected = isPrided == 1
            ivCollect.isSelected = isCollected == 1
            tvCollect.text = if (isCollected == 1) "已收藏" else "收藏"
            if (TextUtils.isEmpty(createStamptime)) {
                llPride.visibility = View.GONE
            }
        }
    }

    private fun initEvent() {
        llCollect.setOnClickListener {
            blog?.apply {
                if (isCollected == 0) {
                    viewModel.collect(mBlogId)
                } else {
                    viewModel.unCollect(mBlogId)
                }
            }

        }
        llPride.clickWithLimit(100) {
            blog?.apply {
                if (isPrided == 0) {
                    viewModel.pride(this)
                } else {
                    viewModel.unPride(this)
                }
            }

        }
        llComment.clickWithLimit {
            BlogHelper.comment(self) { msg ->
                viewModel.comment(mBlogId, msg)
            }
        }
    }

    override fun onReceivedStateEvent(type: Int, msg: String) {
        toast(msg)
        if (commentList.size == 0) {
            multiView.showEmpty()
        }
    }

    //发送eventbus事件，用于更新首页列表
    private fun sendBusEvent(tag: Int, msg: String) {
        EventBus.getDefault().post(BlogEvent(tag, msg))
    }

    companion object {
        //blog和blogId不能同时为空
        fun start(activity: Activity, blog: Blog?, blogId: String = "") {
            if (blog == null && TextUtils.isEmpty(blogId)) {
                return
            }
            val b = Bundle()
            b.putString(BLOG_ID, blogId)
            b.putSerializable(BLOG, blog)
            activity.gotoActivity<BlogDetailActivity>(BUNDLE_KEY, b)
        }
    }

}

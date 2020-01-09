package com.cysion.usercenter.ui.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.clickWithLimit
import com.cysion.other.color
import com.cysion.other.gotoActivity
import com.cysion.usercenter.R
import com.cysion.usercenter.adapter.BlogAdapter
import com.cysion.usercenter.constant.BUNDLE_KEY
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.entity.DetailUserEntity
import com.cysion.usercenter.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_people_info.*

class PeopleInfoActivity : BaseModelActivity<UserViewModel>() {

    private lateinit var mUserId: String
    private var mPeopleInfo: DetailUserEntity? = null
    private lateinit var blogAdapter: BlogAdapter
    private val mBlogs: MutableList<Blog> = mutableListOf()

    override fun getLayoutId(): Int = R.layout.activity_people_info

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        ivClose.clickWithLimit {
            finish()
        }
        rvBloglist.layoutManager = LinearLayoutManager(self)
        blogAdapter = BlogAdapter(mBlogs, self, BlogAdapter.COMMON_PAGE)
        rvBloglist.adapter = blogAdapter
    }

    override fun initData() {
        super.initData()
        val bundleExtra = intent.getBundleExtra(BUNDLE_KEY)
        mUserId = bundleExtra.getString(USER_ID)?:""
        viewModel.getPeopleInfo(mapOf("userid" to mUserId))
    }

    override fun observeModel() {
        viewModel.mLiveDetailInfo.observe(this, Observer {
            mPeopleInfo = it
            fillView()
        })
    }

    override fun onReceivedStateEvent(type: Int, msg: String) {
        multiView.showError()
    }
    fun fillView() {
        mPeopleInfo?.apply {
            tvNickname.text = nickname
            tvDesc.text = selfDesc
            mBlogs.addAll(blogs)
            blogAdapter.notifyDataSetChanged()
            Glide.with(self).load(avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserHead)
        }
    }

    companion object {
        const val USER_ID = "user_id"
        //blog和blogId不能同时为空
        fun start(activity: Activity, userId: String = "") {
            if (TextUtils.isEmpty(userId)) {
                return
            }
            val b = Bundle()
            b.putString(USER_ID, userId)
            activity.gotoActivity<PeopleInfoActivity>(BUNDLE_KEY, b)
        }
    }
}

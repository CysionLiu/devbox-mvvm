package com.cysion.usercenter.ui.fragment

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cysion.ktbox.base.BaseModelFragment
import com.cysion.other.startActivity_ex
import com.cysion.usercenter.R
import com.cysion.usercenter.constant.LOGIN_OUT
import com.cysion.usercenter.event.UserEvent
import com.cysion.usercenter.helper.UserCache
import com.cysion.usercenter.ui.activity.CollectActivitiy
import com.cysion.usercenter.ui.activity.LoginActivity
import com.cysion.usercenter.ui.activity.UserBlogActivity
import com.cysion.usercenter.ui.activity.UserDetailActivity
import com.cysion.usercenter.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_center.*
import org.greenrobot.eventbus.EventBus

class UserFragment : BaseModelFragment<UserViewModel>() {



    override fun getLayoutId(): Int = R.layout.fragment_user_center


    override fun initView() {
        Glide.with(context).load(R.mipmap.place_holder)
            .apply(RequestOptions.circleCropTransform())
            .into(ivUserHead)
        ivUserHead.setOnClickListener {
            toLogin()
        }
        rlInfo.setOnClickListener {
            toLogin()
        }
        rlCollect.setOnClickListener {
            if (TextUtils.isEmpty(UserCache.token)) {
                context.startActivity_ex<LoginActivity>()
            } else {
                context.startActivity_ex<CollectActivitiy>()
            }
        }
        rlBlog.setOnClickListener {
            if (TextUtils.isEmpty(UserCache.token)) {
                context.startActivity_ex<LoginActivity>()
            } else {
                context.startActivity_ex<UserBlogActivity>()
            }
        }
        tvLogout.setOnClickListener {
            UserCache.clear()
            updateUserInfo()
            EventBus.getDefault().post(UserEvent(LOGIN_OUT, ""))
        }
    }

    private fun toLogin() {
        if (TextUtils.isEmpty(UserCache.userId)) {
            context.startActivity_ex<LoginActivity>()
        } else {
            context.startActivity_ex<UserDetailActivity>()
        }
    }

    override fun lazyLoad() {
        super.lazyLoad()
        viewModel.getUserInfo()
    }

    override fun visibleAgain() {
        super.visibleAgain()
        updateUserInfo()
    }

    override fun onResume() {
        super.onResume()
        updateUserInfo()
    }

    private fun updateUserInfo() {
        Glide.with(context).load(UserCache.userInfo?.avatar ?: R.mipmap.place_holder)
            .apply(RequestOptions.circleCropTransform())
            .into(ivUserHead)
        tvNickname.text = UserCache.userInfo?.nickname ?: "未登录"
    }

    override fun observeModel() {
    }

    override fun onStateEventChanged(type: Int, msg: String) {
    }

    override fun getRefreshListenerOrNull()=null
}
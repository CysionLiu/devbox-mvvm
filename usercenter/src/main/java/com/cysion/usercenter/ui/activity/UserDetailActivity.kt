package com.cysion.usercenter.ui.activity

import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.color
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.helper.UserCache
import com.cysion.usercenter.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_userdetail.*

class UserDetailActivity : BaseModelActivity<UserViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_userdetail

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        initTopBar()
        Glide.with(self).load(UserCache.userInfo?.avatar)
            .apply(RequestOptions.circleCropTransform())
            .into(ivAvatar)
        tvUsername.text = UserCache.userInfo?.name
        etNickname.setText(UserCache.userInfo?.nickname ?: "")
        etDesc.setText(UserCache.userInfo?.selfDesc ?: "")
        runOnUiThread {
            etNickname.setSelection(etNickname.text.length)
            etDesc.setSelection(if (etDesc.text.length > 1) etDesc.text.length else 0)
        }
    }

    private fun initTopBar() {
        topbar.apply {
            initElements(right = TopBar.ELEMENT.TEXT)
            setTitle("用户信息")
            setTexts("保存", TopBar.Pos.RIGHT)
            setOnTopBarClickListener { obj, pos ->
                if (pos == TopBar.Pos.LEFT) {
                    finish()
                } else if (pos == TopBar.Pos.RIGHT) {
                    viewModel.updateUserInfo(etNickname.text.toString(), etDesc.text.toString())
                }
            }
        }
    }
    override fun observeModel() {
        viewModel.mLiveUserInfo.observe(this, Observer {
            toast("保存成功")
        })
    }

    override fun onReceivedStateEvent(type: Int, msg: String) {
        toast(msg)
    }
}

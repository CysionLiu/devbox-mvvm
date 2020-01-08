package com.cysion.usercenter.ui.activity

import androidx.lifecycle.Observer
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.color
import com.cysion.other.gotoActivity
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.constant.LOGIN_IN
import com.cysion.usercenter.event.UserEvent
import com.cysion.usercenter.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseModelActivity<UserViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun observeModel() {
        viewModel.mLiveUserInfo.observe(this, Observer {
            toast("登录成功")
            finish()
            EventBus.getDefault().post(UserEvent(LOGIN_IN, ""))
        })
    }

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        topbar.apply {
            setTitle("登录")
            setOnTopBarClickListener { obj, pos ->
                if (pos == TopBar.Pos.LEFT) {
                    finish()
                }
            }
        }
        //简单校验下
        tvLogin.setOnClickListener {
            if (etUsername.text.toString().length <= 3 || etPwd.text.toString().length <= 3) {
                toast("长度不符")
                return@setOnClickListener
            }
            viewModel.login(etUsername.text.toString(), etPwd.text.toString())
        }
        tvRegister.setOnClickListener {
            self.gotoActivity<RegisterActivity>()
            finish()
        }
    }

    override fun onStateEventChanged(type: Int, msg: String) {
        toast(msg)
    }
}

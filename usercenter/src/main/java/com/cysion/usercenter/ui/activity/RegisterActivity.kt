package com.cysion.usercenter.ui.activity

import androidx.lifecycle.Observer
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.color
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseModelActivity<UserViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        topbar.apply {
            setTitle("注册")
            setOnTopBarClickListener { obj, pos ->
                if (pos == TopBar.Pos.LEFT) {
                    finish()
                }
            }
        }
        //简单校验下
        tvLogin.setOnClickListener {
            if (etUsername.text.toString().length <= 3
                    || etPwd.text.toString().length <= 3
                    || etConfirmPwd.text.toString().length <= 3
            ) {
                toast("长度不符")
                return@setOnClickListener
            }
            if (!etPwd.text.toString().equals(etConfirmPwd.text.toString())) {
                toast("两次密码不一致")
                return@setOnClickListener
            }
            viewModel.register(etUsername.text.toString().trim(),
                    etPwd.text.toString().trim(),
                    etConfirmPwd.text.toString())
        }
    }

    override fun observeModel() {
        viewModel.mLiveRegisterState.observe(this, Observer {
            if (it) {
                toast("注册成功")
                finish()
            }
        })
    }

    override fun onStateEventChanged(type: Int, msg: String) {
        toast(msg)
    }
}

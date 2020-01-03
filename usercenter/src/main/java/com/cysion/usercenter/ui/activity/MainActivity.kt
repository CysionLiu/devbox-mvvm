package com.cysion.usercenter.ui.activity

import android.os.Handler
import com.cysion.ktbox.Box
import com.cysion.ktbox.base.BaseFragmentAdapter
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.listener.IRefreshListener
import com.cysion.ktbox.utils.darkTextTheme
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.targetfun._addOnPageChangeListener
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.helper.ListVals
import com.cysion.usercenter.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseModelActivity<MainViewModel>() {

    var lastClickTime = 0L

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun observeModel() {
    }

    override fun getRefreshListenerOrNull(): IRefreshListener? = null

    override fun onStateEventChanged(type: Int, msg: String?) {
    }

    override fun initView() {
        darkTextTheme(Box.color(R.color.white))
        vpContent.offscreenPageLimit = 4
    }

    override fun initData() {
        super.initData()
        viewModel.syncUserInfo()
        vpContent.adapter = BaseFragmentAdapter(this.supportFragmentManager, ListVals.getFragments(), ListVals.getTitles())
        tablayout.setupWithViewPager(vpContent)
        initTabs()
        vpContent._addOnPageChangeListener {
            _onPageSelected {
                when (it) {
                    2 -> {
                        whiteTextTheme(Box.color(R.color.dark))
                    }
                    3 -> {
                        whiteTextTheme(Box.color(R.color.colorAccent))
                    }
                    else -> {
                        darkTextTheme(Box.color(R.color.white))
                    }
                }
            }
        }

    }

    //初始化tab，设置图标和自定义布局，注意顺序和某些语句。
    private fun initTabs() {
        val icons = ListVals.getIcons()
        for (i in 0 until tablayout.getTabCount()) {
            tablayout.getTabAt(i)!!.setIcon(icons[i])
            tablayout.getTabAt(i)!!.setCustomView(R.layout.tabmain_item)
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastClickTime >= 2000) {
            toast("再点击一次即可退出")
            lastClickTime = System.currentTimeMillis()
            return
        }
        finish()
    }
}


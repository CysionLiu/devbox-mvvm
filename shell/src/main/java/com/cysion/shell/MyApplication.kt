package com.cysion.shell

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.cysion.ktbox.Box
import com.cysion.ktbox.utils.logd
import com.scwang.smartrefresh.header.DeliveryHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import io.github.prototypez.appjoint.core.AppSpec


@AppSpec
class MyApplication : Application() {

    companion object {
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator(object :DefaultRefreshHeaderCreator{
                override fun createRefreshHeader(context: Context, layout: RefreshLayout): RefreshHeader {
                    return DeliveryHeader(context)
                }
            })
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator{
                override fun createRefreshFooter(context: Context, layout: RefreshLayout): RefreshFooter {
                    return BallPulseFooter(context)
                }
            })
        }
    }

    //该方法执行结束，会调用其它application子类的onCreate方法
    override fun onCreate() {
        super.onCreate()
        Box.init(this,true)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStarted(activity: Activity) {
                logd("flag--MyApplication:onActivityStarted:${activity.localClassName}")
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
                logd("flag--MyApplication:onActivityStopped:${activity.localClassName}")
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityResumed(activity: Activity) {
            }
        })

    }
}
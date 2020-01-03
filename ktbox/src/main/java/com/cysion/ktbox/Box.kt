@file:Suppress("DEPRECATION")

package com.cysion.ktbox

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable

/**
 * Created by cysion on 2017\12\22 0022.
 * 主要放置相关的配置信息，简短使用
 */

@SuppressLint("StaticFieldLeak")
object Box {

    lateinit var context: Context
        private set
    private var width: Int = 0
    private var height: Int = 0
    private var cfg: Configuration? = null
    private var res: Resources? = null
    var debug:Boolean = false
        private set

    //Application创建时就需要立即调用此方法；最高优先级
    fun init(aContext: Context,aDebug:Boolean) {
        debug = aDebug
        context = aContext.applicationContext
        initSize()
        res = context.resources
        cfg = res!!.configuration
    }

    private fun initSize() {
        val dm = context.resources.displayMetrics
        width = dm.widthPixels
        height = dm.heightPixels
    }

    //上下文
    fun ctx(): Context {
        requireNotNull(context) { "应在Application中首先调用Box.init()方法" }
        return context
    }


    //屏幕宽
    fun width(): Int {
        return width
    }

    //屏幕高
    fun height(): Int {
        return height
    }

    //app配置
    fun cfg(): Configuration? {
        return cfg
    }

    //resources
    fun res(): Resources? {
        return res
    }

    //string
    fun str(sid: Int, vararg formatArgs: Any): String {
        return res!!.getString(sid, *formatArgs)
    }

    //color
    fun color(sid: Int): Int {
        return res!!.getColor(sid)
    }

    //drawable
    fun img(sid: Int): Drawable {
        return res!!.getDrawable(sid)
    }

    //drawable
    fun density(): Float {
        return res()!!.displayMetrics.density
    }


    //version code
    fun vcode(): Int {

        val packManager = context.packageManager
        var packageInfo: PackageInfo?
        try {
            packageInfo = packManager.getPackageInfo(context.packageName, 0)
            return packageInfo!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 999999
    }


    //version name
    @Throws(PackageManager.NameNotFoundException::class)
    fun vName(): String {
        val packManager = context.packageManager
        val packageInfo = packManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    }

}

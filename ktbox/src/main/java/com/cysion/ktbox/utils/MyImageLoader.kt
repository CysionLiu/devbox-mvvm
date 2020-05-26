package com.cysion.ktbox.utils

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lzy.imagepicker.loader.ImageLoader

class MyImageLoader:ImageLoader {
    override fun clearMemoryCache() {

    }

    override fun displayImage(activity: Activity?, uri: Uri?, imageView: ImageView?, awidth: Int, aheight: Int) {
        activity?:return
        imageView?:return
        Glide.with(activity).load(uri).apply(object :RequestOptions(){
            override fun override(width: Int, height: Int): RequestOptions {
                return super.override(awidth, aheight)
            }
        }).into(imageView)
    }
}
package com.cysion.media.comm.provider

import android.app.Activity
import android.os.Bundle
import com.cysion.media.constant.BUNDLE_KEY
import com.cysion.media.constant.CHANNEL_NAME
import com.cysion.media.constant.LINK
import com.cysion.media.constant.TITLE
import com.cysion.media.ui.activity.ChannelDetailActivity
import com.cysion.media.ui.activity.NewsDetailActivity
import com.cysion.other.gotoActivity
import com.cysion.router.media.service.ActivityApi
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class ActivityApiIml : ActivityApi {
    override fun startNewsActivity(src: Activity, title: String, link: String) {
        val bundle = Bundle()
        bundle.putString(TITLE, title)
        bundle.putString(LINK, link)
        src.gotoActivity<NewsDetailActivity>(BUNDLE_KEY, bundle)
    }

    override fun startSongsActivity(src: Activity, title: String, channelName: String) {
        val bundle = Bundle()
        bundle.putString(TITLE, title)
        bundle.putString(CHANNEL_NAME, channelName)
        src.gotoActivity<ChannelDetailActivity>(BUNDLE_KEY, bundle)
    }
}
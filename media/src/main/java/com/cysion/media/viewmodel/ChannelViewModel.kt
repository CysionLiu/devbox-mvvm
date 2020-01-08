package com.cysion.media.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.media.entity.ChannelInfo
import com.cysion.media.net.ChannelCaller

class ChannelViewModel:BaseViewModel() {

    val mLiveChannels = MutableLiveData<MutableList<ChannelInfo>>()

    fun getChnList() {
        launchWithFilter(
                {
                    ChannelCaller.api.getChannelList()
                },
                {
                    mLiveChannels.postValue(it[0].channellist)
                },
                showLoading = true
        )
    }
}
package com.cysion.media.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cysion.ktbox.base.BaseViewModel
import com.cysion.media.entity.Song
import com.cysion.media.net.ChannelCaller

class SongViewModel:BaseViewModel() {

    val mLiveSongs = MutableLiveData<MutableList<Song>>()

    fun getSongs(channelName: String) {
        launchWithFilter(
                {
                    ChannelCaller.api.getChannelDetail(channelName)
                },
                {
                    mLiveSongs.postValue(it.songlist)
                }
        )
    }
}
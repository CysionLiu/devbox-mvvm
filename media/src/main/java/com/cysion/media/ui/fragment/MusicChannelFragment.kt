package com.cysion.media.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.cysion.ktbox.base.BaseModelFragment
import com.cysion.ktbox.base.ITEM_CLICK
import com.cysion.ktbox.net.ErrorStatus
import com.cysion.media.R
import com.cysion.media.adapter.MusicChnAdapter
import com.cysion.media.constant.BUNDLE_KEY
import com.cysion.media.constant.CHANNEL_NAME
import com.cysion.media.constant.TITLE
import com.cysion.media.entity.ChannelInfo
import com.cysion.media.ui.activity.ChannelDetailActivity
import com.cysion.media.viewmodel.ChannelViewModel
import com.cysion.other.gotoActivity
import com.cysion.uibox.toast.toast
import kotlinx.android.synthetic.main.fragment_music_chn.*

class MusicChannelFragment : BaseModelFragment<ChannelViewModel>(){

    private val mChannelList = mutableListOf<ChannelInfo>()

    private val adapter by lazy {
        MusicChnAdapter(mChannelList, context as Context)
    }

    override fun getLayoutId(): Int = R.layout.fragment_music_chn

    override fun initView() {
        rvChnList.layoutManager = GridLayoutManager(context, 2)
        rvChnList.adapter = adapter
        adapter.setOnTypeClickListener { obj, position, flag ->
            if (flag == ITEM_CLICK) {
                val bundle = Bundle()
                bundle.putString(TITLE, obj.name)
                bundle.putString(CHANNEL_NAME, obj.ch_name)
                context?.gotoActivity<ChannelDetailActivity>(BUNDLE_KEY, bundle)
            }
        }
    }

    override fun observeModel() {
        viewModel.mLiveChannels.observe(this, Observer {
            mChannelList.addAll(it)
            multiView.showContent()
            adapter.notifyDataSetChanged()
            if (mChannelList.size == 0) {
                multiView.showEmpty()
            }
        })
    }

    override fun lazyLoad() {
        super.lazyLoad()
        viewModel.getChnList()
    }

    override fun visibleAgain() {
        super.visibleAgain()
        if (mChannelList.size == 0) {
            viewModel.getChnList()
        }
    }

    override fun onStateEventChanged(type: Int, msg: String) {
        toast(msg)
        if (mChannelList.size == 0) {
            multiView.showEmpty()
            if (type == ErrorStatus.NETWORK_ERROR) {
                multiView.showNoNetwork()
            }
        }
    }
}
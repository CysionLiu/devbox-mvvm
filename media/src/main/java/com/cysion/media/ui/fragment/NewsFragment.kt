package com.cysion.media.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cysion.ktbox.base.BaseModelFragment
import com.cysion.ktbox.base.ITEM_CLICK
import com.cysion.ktbox.net.ErrorStatus
import com.cysion.media.R
import com.cysion.media.adapter.NewsAdapter
import com.cysion.media.constant.BUNDLE_KEY
import com.cysion.media.constant.LINK
import com.cysion.media.constant.TITLE
import com.cysion.media.entity.NewsInfoEntity
import com.cysion.media.ui.activity.NewsDetailActivity
import com.cysion.media.viewmodel.NewsViewModel
import com.cysion.other.gotoActivity
import com.cysion.uibox.toast.toast
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseModelFragment<NewsViewModel>() {

    private var mdatalist: MutableList<NewsInfoEntity> = mutableListOf()
    val adapter by lazy {
        NewsAdapter(mdatalist, activity as Context)
    }

    override fun getLayoutId(): Int = R.layout.fragment_news

    override fun initView() {
        rvNewsList.layoutManager = LinearLayoutManager(activity)
        rvNewsList.adapter = adapter
        adapter.setOnTypeClickListener { obj, position, flag ->
            if (flag == ITEM_CLICK) {
                val bundle = Bundle()
                bundle.putString(TITLE, obj.title)
                bundle.putString(LINK, obj.path)
                context?.gotoActivity<NewsDetailActivity>(BUNDLE_KEY, bundle)
            }
        }
    }

    override fun observeModel() {
        viewModel.mLiveNews.observe(this, Observer {
            mdatalist.addAll(it)
            adapter.notifyDataSetChanged()
            if (mdatalist.size==0) {
                multiView.showEmpty()
            }
        })
    }

    override fun onReceivedStateEvent(type: Int, msg: String) {
        toast(msg)
        if (mdatalist.size == 0) {
            multiView.showEmpty()
            if (type == ErrorStatus.NETWORK_ERROR) {
                multiView.showNoNetwork()
            }
        }
    }
    override fun lazyLoad() {
        super.lazyLoad()
        viewModel.getNewsList()
    }

    override fun visibleAgain() {
        super.visibleAgain()
        if (mdatalist.size == 0) {
            viewModel.getNewsList()
        }
    }
}
package com.cysion.usercenter.ui.activity

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cysion.ktbox.base.BaseModelActivity
import com.cysion.ktbox.base.ITEM_CLICK
import com.cysion.ktbox.net.ErrorStatus
import com.cysion.ktbox.utils.whiteTextTheme
import com.cysion.other.color
import com.cysion.uibox.bar.TopBar
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.adapter.BlogAdapter
import com.cysion.usercenter.constant.COLLECT_CANCEL
import com.cysion.usercenter.entity.Blog
import com.cysion.usercenter.event.BlogEvent
import com.cysion.usercenter.viewmodels.CollectPageViewModel
import com.cysion.wedialog.WeDialog
import kotlinx.android.synthetic.main.activity_user_blogs.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CollectActivitiy : BaseModelActivity<CollectPageViewModel>() {

    private lateinit var blogAdapter: BlogAdapter
    private val mBlogs: MutableList<Blog> = mutableListOf()

    override fun getLayoutId(): Int = R.layout.activity_user_blogs

    override fun initView() {
        whiteTextTheme(color(R.color.colorAccent))
        topbar.apply {
            setTitle("我的收藏")
            setOnTopBarClickListener { obj, pos ->
                if (pos == TopBar.Pos.LEFT) {
                    finish()
                }
            }
        }
        initRV()
    }

    private fun initRV() {
        rvBloglist.layoutManager = LinearLayoutManager(self)
        blogAdapter = BlogAdapter(mBlogs, self, BlogAdapter.COLLECT)
        rvBloglist.adapter = blogAdapter
        blogAdapter.setOnTypeClickListener { obj, position, flag ->
            if (flag == ITEM_CLICK) {
                BlogDetailActivity.start(self, null, obj.blogId)
            } else if (flag == BlogAdapter.DEL) {
                WeDialog.normal(self)
                        .setTitle("提示")
                        .setMsg("确认取消这个收藏吗？")
                        .show {
                            it.dismissAllowingStateLoss()
                            viewModel.delCol(obj.blogId)
                        }
            }
        }
    }

    override fun observeModel() {
        viewModel.mLiveCancleState.observe(this, Observer {
            if (it) {
                initData()
            }
        })
        viewModel.mLiveCollectList.observe(this, Observer {
            mBlogs.clear()
            it.forEach {
                val tmp = Blog(
                        it.authorId, it.itemId, "", it.coverImg,
                        1, 0, "",
                        0, 0, "", it.itemTitle, it.isLargeIcon, 0
                        , "", ""
                )
                mBlogs.add(tmp)
            }
            blogAdapter.notifyDataSetChanged()
            if (mBlogs.size == 0) {
                multiView.showEmpty()
            }
        })

    }

    override fun initData() {
        super.initData()
        viewModel.getBlogs()
    }


    override fun onStateEventChanged(type: Int, msg: String) {
        toast(msg)
        if (mBlogs.size == 0) {
            multiView.showEmpty()
            if (type == ErrorStatus.NETWORK_ERROR) {
                multiView.showNoNetwork()
            }
        }
    }

    //接收BlogEvent事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receive(event: BlogEvent) {
        when (event.tag) {
            COLLECT_CANCEL ->
                initData()
        }
        blogAdapter.notifyDataSetChanged()
    }
}

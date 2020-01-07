package com.cysion.usercenter.ui.fragment

import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cysion.ktbox.base.BaseModelFragment
import com.cysion.ktbox.base.ITEM_CLICK
import com.cysion.ktbox.listener.IRefreshListener
import com.cysion.other._setOnClickListener
import com.cysion.other.dp2px
import com.cysion.other.startActivity_ex
import com.cysion.uibox.toast.toast
import com.cysion.usercenter.R
import com.cysion.usercenter.adapter.BlogAdapter
import com.cysion.usercenter.adapter.HomeTopPageAdapter
import com.cysion.usercenter.communicate.Resolver.mediaActivityApi
import com.cysion.usercenter.constant.*
import com.cysion.usercenter.event.BlogEvent
import com.cysion.usercenter.event.UserEvent
import com.cysion.usercenter.helper.BlogHelper
import com.cysion.usercenter.helper.UserCache
import com.cysion.usercenter.ui.activity.BlogDetailActivity
import com.cysion.usercenter.ui.activity.BlogEditorActivity
import com.cysion.usercenter.ui.activity.LoginActivity
import com.cysion.usercenter.viewmodels.SqureViewModel
import com.tmall.ultraviewpager.UltraViewPager
import kotlinx.android.synthetic.main.fragment_square.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SquareFragment : BaseModelFragment<SqureViewModel>() {

    private lateinit var topAdapter: HomeTopPageAdapter
    private lateinit var blogAdapter: BlogAdapter
    private var curPage = 1
    private val mCarousels by lazy {
        viewModel.mLiveCarousel.value!!
    }
    private val mBlogs by lazy {
        viewModel.mLiveBlogs.value!!
    }

    override fun getLayoutId(): Int = R.layout.fragment_square

    override fun initView() {
        initRefreshLayout()
        initViewPager()
        initRecyclerView()
        initFab()
    }

    override fun observeModel() {
        viewModel.mLiveCarousel.observe(this, Observer {
            ultraViewPager.refresh()
        })
        viewModel.mLiveBlogs.observe(this, Observer {
            curPage++
            val index = mBlogs.size
            if (index <= 10) {
                blogAdapter.notifyDataSetChanged()
            } else {
                blogAdapter.notifyItemRangeChanged(index-10, 10)
            }
            multiView.showContent()
        })
        viewModel.mLivePride.observe(this, Observer {
            blogAdapter.notifyItemChanged(it)
        })
    }

    //    初始化刷新控件
    private fun initRefreshLayout() {
        smartLayout.setOnRefreshListener {
            curPage = 1
            viewModel.getCarousel()
            viewModel.getBlogs(curPage)
            smartLayout.setEnableLoadMore(true)
            fl_load_state.visibility = View.GONE
        }
        smartLayout.setOnLoadMoreListener {
            fl_load_state.visibility = View.GONE
            viewModel.getBlogs(curPage)
        }
    }

    //    初始化顶部轮播
    private fun initViewPager() {
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        topAdapter = HomeTopPageAdapter(context, mCarousels)
        ultraViewPager.adapter = topAdapter
        ultraViewPager.initIndicator()
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.RED)
                .setNormalColor(Color.WHITE)
                .setRadius(context.dp2px(3).toInt())
        ultraViewPager.apply {
            indicator.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
                    .setMargin(0, 0, 0, context.dp2px(10).toInt())
                    .build()
            setInfiniteLoop(true)
            ultraViewPager.setAutoScroll(3000)
        }
        topAdapter.setItemClickListener {
            if (it.type.equals("news")) {
                mediaActivityApi.startNewsActivity(context, it.title, it.link)
            } else if (it.type.equals("music")) {
                mediaActivityApi.startSongsActivity(context, it.title, it.mediaId)
            } else if (it.type.equals("blog")) {
                BlogDetailActivity.start(context, null, it.mediaId)
            }
        }
    }

    //初始化列表
    private fun initRecyclerView() {
        rvBloglist.isNestedScrollingEnabled = false
        blogAdapter = BlogAdapter(mBlogs, context)
        rvBloglist.adapter = blogAdapter
        rvBloglist.layoutManager = LinearLayoutManager(context)
        blogAdapter.setOnTypeClickListener { obj, position, flag ->
            if (flag == ITEM_CLICK) {
                BlogDetailActivity.start(context, obj)
            } else if (flag == BlogAdapter.PRIDE) {
                if (obj.isPrided == 1) {
                    viewModel.unPride(obj, position)
                } else {
                    viewModel.pride(obj, position)
                }
            }
        }
    }

    //设置fab button点击
    private fun initFab() {
        fabBtn._setOnClickListener {
            if (TextUtils.isEmpty(UserCache.token)) {
                context.startActivity_ex<LoginActivity>()
            } else {
                BlogEditorActivity.start(context, "", "")
            }
        }
    }

    //加载数据
    override fun initData() {
        super.initData()
        smartLayout.autoRefresh()
    }

    override fun onStateEventChanged(type: Int, msg: String) {
        toast(msg)
        //已获得所有数据
        if (type == 400) {
            fl_load_state.visibility = View.VISIBLE
            tvLoadFinish.visibility = View.VISIBLE
            tvLoadFail.visibility = View.GONE
            smartLayout.setEnableLoadMore(false)
        } else {
            fl_load_state.visibility = View.VISIBLE
            tvLoadFinish.visibility = View.GONE
            tvLoadFail.visibility = View.VISIBLE
        }
    }

    override fun getRefreshListenerOrNull() = object : IRefreshListener {
        override fun onLoadMoreOk(msg: String?) {
            smartLayout.finishLoadMore(200)
        }

        override fun onRefreshFail(msg: String?) {
            smartLayout.finishRefresh(false)
        }

        override fun onRefreshOk(msg: String?) {
            smartLayout.finishRefresh()
        }

        override fun onLoadMoreFail(msg: String?) {
            smartLayout.finishLoadMore(200)
        }
    }

    //接收BlogEvent事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receive(event: BlogEvent) {
        when (event.tag) {
            PRIDE_OK ->
                BlogHelper.getBlog(event.msg, viewModel.mLiveBlogs.value!!)?.apply {
                    isPrided = 1
                    prideCount++
                }
            PRIDE_CANCEL ->
                BlogHelper.getBlog(event.msg, viewModel.mLiveBlogs.value!!)?.apply {
                    isPrided = 0
                    prideCount--
                }
            COLLECT_OK ->
                BlogHelper.getBlog(event.msg, viewModel.mLiveBlogs.value!!)?.isCollected = 1
            COLLECT_CANCEL ->
                BlogHelper.getBlog(event.msg, viewModel.mLiveBlogs.value!!)?.isCollected = 0
            CREATE_BLOG, UPDATE_BLOG, COMMENT, LOGIN_IN, LOGIN_OUT ->
                smartLayout.autoRefresh()
        }
        blogAdapter.notifyDataSetChanged()
    }

    //接收UserEvent事件，登录登出
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receive(event: UserEvent) {
        when (event.tag) {
            LOGIN_IN, LOGIN_OUT ->
                smartLayout.autoRefresh()
        }
    }
}

package com.ywc.recycler.scroll;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.io.OnScollCall;
import com.ywc.recycler.mode.ScrollMode;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by Administrator on 2018/7/18.
 */

public class CustomScroll extends RecyclerView.OnScrollListener{

    //是否在加载中
    private boolean is_load;
    //是否向上滑动
    private boolean slideUp=true;

    public void setSlideUp(boolean slideUp) {
        this.slideUp = slideUp;
    }

    //是否在滚动
    private boolean is_scroll;
    //最后一个item的position
    private int lastPosition;
    //滑动允许状态
    private ScrollMode scrollMode;

    public ScrollMode getScrollMode() {
        return scrollMode;
    }

    public void setScrollMode(ScrollMode scrollMode) {
        this.scrollMode = scrollMode;
    }

    //用于延迟
    private Handler handler=new Handler();
    private OnScollCall onScollCall;
    private CustomAdapter customAdapter;

    public CustomScroll(OnScollCall onScollCall,CustomAdapter adapter) {
        this.onScollCall = onScollCall;
        this.customAdapter=adapter;
    }

    //滑动移动位置
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        is_scroll=true;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
            lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        else if(layoutManager instanceof LinearLayoutManager)
            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    //滑动状态
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState)
        {
            //停止滚动
            case SCROLL_STATE_IDLE:
                if (customAdapter!=null)
                    initStop(recyclerView);
                break;
            //正在滚动
            case SCROLL_STATE_TOUCH_SCROLL:
                break;
            //惯性运动
            case SCROLL_STATE_FLING:
                break;
        }
    }

    private void initStop(RecyclerView recyclerView) {

        if (scrollMode==ScrollMode.NULL||scrollMode==ScrollMode.PULL_DOWN||is_load||!slideUp)
            return;
        //之前是否移动，如果前面不移动则返回，如果移动则继续,确保这段代码只执行一次
        if (is_scroll)
        {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //可见数量
            int look_sum = layoutManager.getChildCount();
            //总数量
            int sumItem = layoutManager.getItemCount();

            if (look_sum>0&& this.lastPosition >=sumItem-1)
            {
                is_load=true;
                customAdapter.setLoadLayout(true);
                //使得recycley调到最后
                recyclerView.smoothScrollToPosition(customAdapter.getItemCount() - 1);
                //加载中和刷新布局
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onScollCall.callback(ScrollMode.PULL_UP);
                    }
                },100);
            }
        }
        else
            is_scroll=false;

    }
}

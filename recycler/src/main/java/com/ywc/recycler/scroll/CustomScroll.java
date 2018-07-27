package com.ywc.recycler.scroll;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.io.OnScrollCall;
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
    //是否可以运行，没有加载和向下滑动
    private boolean is_run=true;

    public void setIs_run(boolean is_run) {
        this.is_run = is_run;
    }

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
    private OnScrollCall onScollCall;
    private CustomAdapter customAdapter;

    public CustomScroll(OnScrollCall onScollCall, CustomAdapter adapter) {
        this.onScollCall = onScollCall;
        this.customAdapter=adapter;
    }


    //滑动状态
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState==SCROLL_STATE_IDLE&&customAdapter!=null)
            initStop(recyclerView);
    }

    private void initStop(RecyclerView recyclerView) {
        //可见的数量
        int lookCount = recyclerView.getLayoutManager().getChildCount();
        if (scrollMode==ScrollMode.NULL||scrollMode==ScrollMode.PULL_DOWN||is_load||!is_run||lookCount==0)
            return;
        //之前是否移动，如果前面不移动则返回，如果移动则继续,确保这段代码只执行一次
        if (!recyclerView.canScrollVertically(1))
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
}

package com.ywc.recycler;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.ScrollMode;
import com.ywc.recycler.scroll.CustomScroll;

/**
 * Created by Administrator on 2018/7/27.
 */

public class CustomScrollView extends NestedScrollView{
    private RecyclerView recyclerView;
    private boolean is_load;
    private ScrollMode scrollMode;

    public void setScrollMode(ScrollMode scrollMode) {
        this.scrollMode = scrollMode;
    }

    private Handler handler=new Handler();

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(final RecyclerView recyclerView, final OnScrollCall onScrollCall)
    {
        this.recyclerView=recyclerView;
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY-oldScrollY>0&&(scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_UP))
                {
                    onScroll(onScrollCall);
                }
            }
        });

    }

    private void onScroll(final OnScrollCall onScrollCall)
    {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof CustomAdapter)
        {
            CustomAdapter customAdapter = (CustomAdapter) adapter;
            int lastPosition = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager)
                lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            else if (layoutManager instanceof LinearLayoutManager)
                lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            else
                return;
            //可见数量
            int visibleItemCount = layoutManager.getChildCount();
            //总数量
            int totalItemCount = layoutManager.getItemCount();
            if (  visibleItemCount>0&& lastPosition >=totalItemCount-1&&!is_load)
            {
                is_load=true;
                customAdapter.setLoadLayout(true);
                //使得recycley调到最后
                recyclerView.smoothScrollToPosition(customAdapter.getItemCount() - 1);
                //加载中和刷新布局
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onScrollCall.callback(ScrollMode.PULL_UP);
                    }
                },100);
            }
        }


    }


}

package com.ywc.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.ScrollMode;
import com.ywc.recycler.scroll.CustomScroll;


/**
 * Created by Administrator on 2018/7/18.
 */

public class CustomRecycler extends RecyclerView{
    private CustomScroll customScroll;
    private CustomAdapter customAdapter;
    public CustomRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomRecycler(Context context) {
        super(context);
    }

    //滑动监听
    public void with(ScrollMode scrollMode,LayoutManager layout, Adapter adapter,  OnScrollCall onScollCall)
    {
        setLayoutManager(layout);
        if (adapter instanceof CustomAdapter)
        {
            customAdapter=((CustomAdapter) adapter);
            customScroll=new CustomScroll(onScollCall,customAdapter );
            addOnScrollListener(customScroll);
            setScrollMode(scrollMode);
        }
        setAdapter(adapter);
        setScrollMode(ScrollMode.NULL);
    }

    public void setScrollMode(ScrollMode scrollMode)
    {
        if (customScroll!=null)
            customScroll.setScrollMode(scrollMode);
    }

    //是否在刷新
    public void setIs_run(boolean is_run)
    {
        if (customScroll!=null)
            customScroll.setIs_run(is_run);
    }

    //刷新截图
    public void onScrollFinish()
    {
        if (customAdapter!=null)
        {
            //删除没有更多
            customAdapter.setNullLayout(false);
            //删除加载更多
            customAdapter.setLoadLayout(false);
            //接触加载中
            customScroll.setIs_load(false);
        }
    }

    //添加没有更多
    public void addNull()
    {
        if (customAdapter!=null)
            customAdapter.setNullLayout(true);
    }


    public void addHead(View view)
    {
        if (customAdapter!=null)
            customAdapter.addHead(view);
    }

    public void addFoot(int layoutId)
    {
        if (customAdapter!=null)
            customAdapter.addFoot(layoutId);
    }

    public void removeHead(View view)
    {
        if (customAdapter!=null)
            customAdapter.removeHead(view);
    }

    public void removeFoot(int layoutId)
    {
        if (customAdapter!=null)
            customAdapter.removeFoot(layoutId);
    }


    public int initRecycler()
    {
        getLayoutManager().scrollToPosition(0);
        setScrollMode(ScrollMode.NULL);
        return 1;
    }

}

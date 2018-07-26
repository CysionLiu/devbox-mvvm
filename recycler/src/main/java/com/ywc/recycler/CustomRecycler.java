package com.ywc.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.io.OnScollCall;
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
    public void addOnScoll(Adapter adapter, ScrollMode scrollMode, OnScollCall onScollCall)
    {
        if (adapter instanceof CustomAdapter)
        {
            customAdapter=((CustomAdapter) adapter);
            customScroll=new CustomScroll(onScollCall,customAdapter );
            addOnScrollListener(customScroll);
            setScollMode(scrollMode);
        }
        setAdapter(adapter);
    }

    public void setScollMode(ScrollMode scrollMode)
    {
        if (customScroll!=null)
            customScroll.setScrollMode(scrollMode);
    }

    //是否向上滑动
    public void setSlideUp(boolean slideUp)
    {
        if (customScroll!=null)
            customScroll.setSlideUp(slideUp);
    }

    //删除没有更多
    public void removeLoad()
    {
        if (customAdapter!=null)
            customAdapter.setLoadLayout(false);
    }

    public void removeNull()
    {
        if (customAdapter!=null)
            customAdapter.setNullLayout(false);
    }

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
}

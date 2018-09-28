package com.ywc.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.LoadMode;
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
            if (scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_UP)
            {
                customScroll=new CustomScroll(onScollCall,customAdapter );
                addOnScrollListener(customScroll);
                setScrollMode(scrollMode);
            }
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
            if (customScroll!=null)
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
        if (view!=null&&customAdapter!=null)
        {
            customAdapter.addHead(view);
        }
    }

    public void removeHead(View view)
    {
        if (view!=null&&customAdapter!=null)
            customAdapter.removeHead(view);
    }


    public void addFoot(int layoutId)
    {
        if (customAdapter!=null)
            customAdapter.addFoot(layoutId);
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





    public void onEndHandler1(int size,LoadMode mode,View view)
    {
        Log.d("H","纯白梦");
        onScrollFinish();
        addHead(view);
        if (size==0)
        {
            setScrollMode(ScrollMode.NULL);
            if (mode==LoadMode.PULL_UP)
                addNull();
            else
                removeHead(view);
        }
        else if (size<ConfigUtils.dataSize)
        {
            setScrollMode(ScrollMode.NULL);
            if (mode==LoadMode.PULL_UP)
                addNull();
        }
        else
        {
            setScrollMode(ScrollMode.PULL_UP);
        }
    }




    public void onEndHandler2(int size,LoadMode mode,View nullView)
    {
        onScrollFinish();
        if (size==0&&mode!=LoadMode.PULL_UP)
        {
            this.setVisibility(GONE);
            nullView.setVisibility(VISIBLE);
        }
        else
        {
            this.setVisibility(VISIBLE);
            nullView.setVisibility(GONE);
            if (size==0)
            {
                setScrollMode(ScrollMode.NULL);
                addNull();
            }
            else if (size<ConfigUtils.dataSize)
            {
                setScrollMode(ScrollMode.NULL);
                if (mode==LoadMode.PULL_UP)
                    addNull();
            }
            else
            {
                setScrollMode(ScrollMode.PULL_UP);
            }
        }
    }


    private void clear()
    {
        if (customAdapter!=null)
            customAdapter.clear();
    }


    //这个成功是参数大于0 ,size >0 表示成功，大于datasize表示还有数据
    public void onEndHandler(int size,LoadMode mode)
    {
        onEndHandler(size,mode,null);
    }

    public void onEndHandler(int size,LoadMode mode,View nullView,View showView)
    {
        onEndHandler(size,mode,nullView);
        if (size==0&&mode!=LoadMode.PULL_UP)
            showView.setVisibility(GONE);
        else
            showView.setVisibility(VISIBLE);
    }

    public void onEndHandler(int size,LoadMode mode,View view)
    {
        onScrollFinish();
        removeHead(view);
        if (size==0)
        {
            setScrollMode(ScrollMode.NULL);
            //如果上滑
            if (mode==LoadMode.PULL_UP)
            {
                addNull();
            }
            else
            {
                addHead(view);
                clear();
            }
        }
        else if (size<ConfigUtils.dataSize)
        {
            setScrollMode(ScrollMode.NULL);
            if (mode==LoadMode.PULL_UP)
                addNull();
        }
        else
        {
            setScrollMode(ScrollMode.PULL_UP);
        }
    }


}

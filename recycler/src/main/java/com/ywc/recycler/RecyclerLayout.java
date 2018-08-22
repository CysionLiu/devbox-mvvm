package com.ywc.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.LoadMode;
import com.ywc.recycler.mode.ScrollMode;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/7/23.
 */

public class RecyclerLayout extends ScrollLayout {

    private CustomRecycler customRecycler;
    //是否允许刷新数据
    public void setScrollMode(ScrollMode scrollMode) {
        super.setScrollMode(scrollMode);
        customRecycler.setScrollMode(scrollMode);
    }

    public CustomRecycler getCustomRecycler() {
        return customRecycler;
    }

    public RecyclerLayout(Context context) {
        super(context);
        init();
    }

    public RecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        //创建Recycler对象,并且赋值空间,范围都是上下充满
        customRecycler = new CustomRecycler(getContext());
        addView(customRecycler,new LayoutParams(PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.MATCH_PARENT));
    }


    //初始化
    public void with(ScrollMode scrollMode, RecyclerView.LayoutManager layout, RecyclerView.Adapter adaptert, final OnScrollCall onScollCall)
    {
        if ((scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_DOWN)&&onScollCall!=null)
        {
            setScroll(onScollCall,customRecycler);
        }
        if ((scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_UP)&&onScollCall!=null)
        {
            customRecycler.with(scrollMode,layout,adaptert,onScollCall);
        }
        else
        {
            customRecycler.setLayoutManager(layout);
            customRecycler.setAdapter(adaptert);
        }
        //第一加载不允许下拉
        super.setScrollMode(ScrollMode.NULL);
    }


    private float actionDownY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        //下拉的时候不允许上啦 ,是否正在刷新
        if (!isRefreshing())
        {
            switch (e.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    actionDownY = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    customRecycler.setIs_run((actionDownY-e.getY())>0);
                    break;
            }
        }
        else
            customRecycler.setIs_run(false);
        return super.dispatchTouchEvent(e);
    }


    public void onScrollFinish()
    {
        super.onScrollFinish();
        customRecycler.onScrollFinish();
    }



    public void addHead(View view)
    {
        customRecycler.addHead(view);
    }

    public void addFoot(int layoutId)
    {
        customRecycler.addFoot(layoutId);
    }

    public void removeHead(View view)
    {
        customRecycler.removeHead(view);
    }

    public void removeFoot(int layoutId)
    {
        customRecycler.removeFoot(layoutId);

    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor)
    {
        customRecycler.addItemDecoration(decor);
    }

    //这个成功是参数大于0 ,size >0 表示成功，大于datasize表示还有数据
    public void onEndHandler(int size,LoadMode mode)
    {
        onScrollFinish();
        if (size==0)
        {
            if (mode==LoadMode.PULL_UP)
            {
                customRecycler.addNull();
                setScrollMode(ScrollMode.PULL_DOWN);
            }
            else
            {
                setScrollMode(ScrollMode.NULL);
            }
        }
        else if (size<ConfigUtils.dataSize)
        {
            setScrollMode(ScrollMode.PULL_DOWN);
            if (mode==LoadMode.PULL_UP)
            {
                customRecycler.addNull();
            }
        }
        else
        {
            setScrollMode(ScrollMode.BOTH);
        }
    }


    public void onEndHandler(int size,LoadMode mode,View view)
    {
        onScrollFinish();
        customRecycler.removeHead(view);
        if (size==0)
        {
            if (mode==LoadMode.PULL_UP)
            {
                customRecycler.addNull();
                setScrollMode(ScrollMode.PULL_DOWN);
            }
            else
            {
                setScrollMode(ScrollMode.NULL);
                customRecycler.addView(view);
            }
        }
        else if (size<ConfigUtils.dataSize)
        {
            setScrollMode(ScrollMode.PULL_DOWN);
            if (mode==LoadMode.PULL_UP)
            {
                customRecycler.addNull();
            }
        }
        else
        {
            setScrollMode(ScrollMode.BOTH);
        }
    }

    //初始化
    public int initRecycler()
    {
        super.setScrollMode(ScrollMode.NULL);
        return  customRecycler.initRecycler();
    }

    //一般时候不用，是滑动监听不给力使用
    public void setAbility(boolean ability) {
        if (customRecycler!=null)
            customRecycler.setAbility(ability);
    }
}

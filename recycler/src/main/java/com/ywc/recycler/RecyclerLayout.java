package com.ywc.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ywc.recycler.io.OnScollCall;
import com.ywc.recycler.mode.ScrollMode;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/7/23.
 */

public class RecyclerLayout extends ScrollLayout {

    private CustomRecycler customRecycler;
    //是否允许刷新数据
    private ScrollMode scrollMode;

    public void setScrollMode(ScrollMode scrollMode) {
        this.scrollMode=scrollMode;
        super.setScrollMode(scrollMode);
        customRecycler.setScollMode(scrollMode);
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
        addView(customRecycler,new PtrFrameLayout.LayoutParams(PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.MATCH_PARENT));
    }


    //初始化
    public void with(ScrollMode scrollMode, RecyclerView.LayoutManager layou, RecyclerView.Adapter adaptert, final OnScollCall onScollCall)
    {
        customRecycler.setLayoutManager(layou);
        if ((scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_DOWN)&&onScollCall!=null)
        {
            setScroll(onScollCall,customRecycler);
        }
        if ((scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_UP)&&onScollCall!=null)
        {
            customRecycler.addOnScoll(adaptert,scrollMode,onScollCall);
        }
        else
        {
            customRecycler.setAdapter(adaptert);
        }
        //第一加载不允许下拉
        setScrollMode(ScrollMode.NULL);
    }


    private float actionDown_Y;
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        //下拉的时候不允许上啦 ,是否正在刷新
        if (!isRefreshing())
        {
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    actionDown_Y = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("h","不错"+(e.getY()-actionDown_Y));
                    //负数是向上
                    customRecycler.setSlideUp((actionDown_Y-e.getY())>0);
                    break;
            }
        }
        else
        {
            customRecycler.setSlideUp(false);
        }
        return super.dispatchTouchEvent(e);
    }


    public void onScrollFinish()
    {
        if (ScrollMode.BOTH == scrollMode || ScrollMode.PULL_DOWN == scrollMode)
            super.onScrollFinish();
        if (ScrollMode.BOTH == scrollMode || ScrollMode.PULL_UP == scrollMode)
            customRecycler.removeLoad();
    }

}

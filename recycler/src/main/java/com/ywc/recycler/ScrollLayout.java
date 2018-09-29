package com.ywc.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.LoadMode;
import com.ywc.recycler.mode.ScrollMode;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2018/7/23.
 */

public class ScrollLayout extends PtrFrameLayout {

    private boolean is_flush;
    public ScrollLayout(Context context) {
        super(context);
        initScroll();
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroll();
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initScroll();
    }

    private void initScroll() {

        //设置下拉的阻尼系数，值越大感觉越难下拉
        setResistance(1.7f);
        //设置超过头部的多少时，释放可以执行刷新操作
        setRatioOfHeaderHeightToRefresh(1.2f);
        //设置下拉回弹的时间
        setDurationToClose(200);
        //设刷新完成，头部回弹时间，注意和前一个进行区别
        setDurationToCloseHeader(1000);
        //设置刷新的时候是否保持头部
        setKeepHeaderWhenRefresh(true);
        //设置下拉过程中执行刷新，我们一般设置为false
        setPullToRefresh(false);
        //ViewPager滑动冲突
        disableWhenHorizontalMove(true);

        ScrollHead scrollHead = new ScrollHead(getContext());
        addPtrUIHandler(scrollHead);
        setHeaderView(scrollHead);
        //初始化不能刷新
        setEnabled(false);
    }


    public void setScroll(final OnScrollCall onScollCall)
    {
        setScroll(onScollCall,null);
    }

    public void setScroll(final OnScrollCall onScollCall, final CustomRecycler customRecycler)
    {
        is_flush=true;
        setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.d("H","下拉刷新");
                onScollCall.callback(LoadMode.PULL_DOWN);
            }
        });
        //初始时候是不能刷新的，避免和第一网络加载产生冲突
    }

    public void setScrollMode(int scrollMode) {
        if ((scrollMode==ScrollMode.BOTH||scrollMode==ScrollMode.PULL_DOWN)&&is_flush)
            setEnabled(true);
        else
            setEnabled(false);
    }

    public void onScrollFinish()
    {
        refreshComplete();
    }

}

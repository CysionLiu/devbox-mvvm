package com.ywc.recycler;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Administrator on 2018/7/23.
 */

public class ScrollHead extends FrameLayout implements PtrUIHandler {

    private TextView tv;
    private ImageView scrollHead_image;
    private TextView scrollHead_text;
    private int new_statue;
    private final int statue_prepare=1;
    private final int statue_start=2;
    private final int statue_finish=3;
    private float scroll_headDimen;
    private AnimationDrawable animationDrawable;

    public ScrollHead(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View scroll_head = LayoutInflater.from(getContext()).inflate(R.layout.scroll_head, this, false);
        scrollHead_image = ((ImageView) scroll_head.findViewById(R.id.scrollHead_image));
        scrollHead_text = ((TextView) scroll_head.findViewById(R.id.scrollHead_text));
        addView(scroll_head);
        //高度
        scroll_headDimen = getResources().getDimension(R.dimen.scroll_head);
        scrollHead_image.setImageResource(ConfigUtils.anim_id);
        animationDrawable = ((AnimationDrawable) scrollHead_image.getDrawable());
    }



    //重置
    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    //准备
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        new_statue=statue_prepare;
    }

    //开始  开启动画
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        new_statue=statue_start;
        if (!animationDrawable.isRunning())
            animationDrawable.start();
    }

    //结束  关闭动画
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        new_statue=statue_finish;
        if (animationDrawable.isRunning())
            animationDrawable.stop();
    }

    //头部布局滑动变化
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        switch (new_statue) {
            case statue_prepare:
                //logo设置
                if (ptrIndicator.getCurrentPercent() <= 1) {
                    Integer size = (int) (scroll_headDimen * ptrIndicator.getCurrentPercent());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
                    scrollHead_image.setLayoutParams(layoutParams);
                }
                if (ptrIndicator.getCurrentPercent() < 1.2)
                    scrollHead_text.setText(ConfigUtils.head_init);
                else
                    scrollHead_text.setText(ConfigUtils.head_prepare);
                break;
            case statue_start:
                scrollHead_text.setText(ConfigUtils.head_start);
                break;
            case statue_finish:
                scrollHead_text.setText(ConfigUtils.head_finish);
                break;
        }

    }
}

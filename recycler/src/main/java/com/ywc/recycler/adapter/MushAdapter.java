package com.ywc.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ywc.recycler.holder.BaseViewHold;

/**
 * Created by Administrator on 2018/8/3.
 * 多种布局
 */

public abstract class MushAdapter<T> extends CustomAdapter<T>{

    private int itemLayouts[];
    public MushAdapter(Context context,int itemLayouts[]) {
        super(context);
        this.itemLayouts=itemLayouts;
    }

    //这个布局来控制多种不同的布局
    @Override
    public int getListType(int position) {
        return super.getListType(position);
    }


    @Override
    public BaseViewHold onCreateListHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < itemLayouts.length; i++) {
            if (viewType==i)
            {
                return new BaseViewHold(LayoutInflater.from(getContext()).inflate(itemLayouts[i], parent, false));
            }
        }
        return new BaseViewHold(LayoutInflater.from(getContext()).inflate(itemLayouts[0], parent, false));
    }

}

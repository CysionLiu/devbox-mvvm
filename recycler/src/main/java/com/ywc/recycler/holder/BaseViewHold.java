package com.ywc.recycler.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2018/7/18.
 */

public class BaseViewHold extends RecyclerView.ViewHolder{

    private SparseArray<View> viewArray;
    public BaseViewHold(View itemView) {
        super(itemView);
        viewArray=new SparseArray<>();
    }

    //设置缓存，查找控件
    public <T extends View> T fdView(int id)
    {
        View view = viewArray.get(id);
        if (view==null)
        {
            view=itemView.findViewById(id);
            viewArray.put(id,view);
        }
        return (T) view;
    }



}

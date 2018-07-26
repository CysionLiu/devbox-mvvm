package com.ywc.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ywc.recycler.holder.BaseViewHold;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHold>{

    protected List<T> listData;
    private int itemLayout;
    protected Context context;

    public BaseAdapter(List<T> listData, int itemLayout, Context context) {
        this.listData = listData;
        this.itemLayout = itemLayout;
        this.context = context;
    }

    //默认值是0，head和foot和加载数据 都采用
    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHold(LayoutInflater.from(context).inflate(itemLayout,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseViewHold holder, int position) {
        T t = listData.get(position);
        findbyid(holder,position,t);
        setData(listData.get(position));
        itemListener(holder,position,t);
    }

    protected abstract void findbyid(BaseViewHold holder,int position, T t);
    protected abstract void setData(T t);

    protected void itemListener(BaseViewHold hold,int position,T t)
    {

    }

    public void flush(List<T> list)
    {
        if (listData.size()>0)
            listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    //设置动画
    private boolean is_Anima;


    public void removeT(int position)
    {
        listData.remove(position);
        notifyItemRemoved(position);
    }
}

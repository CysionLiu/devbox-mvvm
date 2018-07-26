package com.ywc.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ywc.recycler.ConfigUtils;
import com.ywc.recycler.holder.BaseViewHold;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public abstract class CustomAdapter<T> extends BaseAdapter<T>{

    //头部布局
    protected List<View> headList=new ArrayList<>();
    protected List<Integer> footList=new ArrayList<>();
    private int min=Integer.MIN_VALUE;

    private boolean loadLayout;
    //如果存在占无更多那么就不会存在加载
    private boolean nullLayout;

    public boolean isLoadLayout() {
        return loadLayout;
    }

    public boolean isNullLayout() {
        return nullLayout;
    }

    public void setLoadLayout(boolean loadLayout) {
        if (!nullLayout&&this.loadLayout !=loadLayout)
        {
            this.loadLayout=loadLayout;
            notifyItemRangeChanged(getItemCount()-getLastCount(),getItemCount());
        }
    }

    public void setNullLayout(boolean nullLayout) {
        if ( this.nullLayout !=nullLayout)
        {
            this.nullLayout=nullLayout;
            notifyItemRangeChanged(getItemCount()-getLastCount(),getItemCount());
        }
    }


    public CustomAdapter(List<T> listData, int itemLayout, Context context) {
        super(listData, itemLayout, context);
    }


    public int getLastCount()
    {
        return nullLayout||loadLayout?1:0;
    }

    @Override
    public int getHeadCount() {
        return headList.size();
    }

    public int getFootCount()
    {
        return footList.size();
    }

    public int getListCount()
    {
        return listData.size();
    }

    @Override
    public int getItemCount() {
        return getHeadCount()+getListCount()+getFootCount()+getLastCount();
    }


    //这里的item不是无限增加的
    @Override
    public int getItemViewType(int position) {
        if (position<getHeadCount())
            return min+position;
        else if (position<getHeadCount()+getListCount())
            return getListType(position-getHeadCount());
        else if (position<getItemCount()-getLastCount())
            return min/2+position-listData.size()-headList.size();
        return nullLayout?-2:-1;
    }

    //为了利于后面的扩张
    public int getListType(int position)
    {
        return super.getItemViewType(position)+getHeadCount();
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType>=0)
            return super.onCreateViewHolder(parent, viewType);
        else if (viewType==-1)
            return new BaseViewHold(LayoutInflater.from(context).inflate(ConfigUtils.load_layoutId,parent, false));
        else if (viewType==-2)
            return new BaseViewHold(LayoutInflater.from(context).inflate(ConfigUtils.null_layoutId,parent, false));
        //头部
        else if (viewType<min/2)
            return  new BaseViewHold(headList.get(viewType-min));
        else
            return new BaseViewHold(LayoutInflater.from(context).inflate(footList.get(viewType-min/2),parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHold holder, int position) {
        if (holder.getItemViewType()>=0)
            super.onBindViewHolder(holder, position);
        else if (holder.getItemViewType()>=min/2&&holder.getItemViewType()<-2)
            onBindFoot(holder,position-getHeadCount()-getListCount());
    }

    protected void onBindFoot(BaseViewHold holder, int position)
    {

    }

    //添加头部
    public void addHead(View view)
    {
        if (!headList.contains(view))
        {
            headList.add(view);
            notifyDataSetChanged();
        }
    }

    public void removeHead(View view)
    {
        if (headList.contains(view))
        {
            int positon = headList.indexOf(view);
            headList.remove(positon);
            notifyDataSetChanged();
        }
    }

    public void addFoot(int layout_id)
    {
        if (!footList.contains(layout_id))
        {
            footList.add(layout_id);
            notifyItemRangeChanged(getHeadCount()+getListCount(),getItemCount());
        }
    }

    public void removeFoot(int layout_id)
    {
        if (footList.contains(layout_id))
        {
            int positon = footList.indexOf(layout_id);
            footList.remove(positon);
            notifyItemRangeChanged(getHeadCount()+getListCount(),getItemCount());
        }
    }


    //解决grid 添加头尾不占全部得问题
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager)
        {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) >=0 ?  1: gridManager.getSpanCount();
                }
            });
        }
    }
}

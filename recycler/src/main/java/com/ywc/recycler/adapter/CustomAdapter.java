package com.ywc.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public static int load_layoutId;
    private int min=Integer.MIN_VALUE;

    public boolean is_showLoad() {
        return is_showLoad;
    }

    public void setIs_showLoad(boolean is_showLoad) {
        this.is_showLoad = is_showLoad;
        notifyDataSetChanged();
    }

    private boolean is_showLoad;

    public CustomAdapter(List<T> listData, int itemLayout, Context context) {
        super(listData, itemLayout, context);
    }

    @Override
    public int getItemCount() {
        return headList.size()+footList.size()+listData.size()+(is_showLoad()?1:0);
    }


    //这里的item不是无限增加的
    @Override
    public int getItemViewType(int position) {
        if (position<headList.size())
            return min+position;
        else if (position<headList.size()+listData.size())
            return super.getItemViewType(position);
        else if (position<getItemCount()-(is_showLoad()?1:0))
            return min/2+position-listData.size()-headList.size();
        return -1;
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType>=0)
            return super.onCreateViewHolder(parent, viewType);
        else if (viewType==-1)
            return new BaseViewHold(LayoutInflater.from(context).inflate(load_layoutId,parent, false));
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
        else if (holder.getItemViewType()>=min/2&&holder.getItemViewType()<-1)
            onBindFoot(holder,position);
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
            notifyItemRangeChanged(listData.size()+headList.size(),getItemCount());
        }
    }

    public void removeFoot(int layout_id)
    {
        if (footList.contains(layout_id))
        {
            int positon = footList.indexOf(layout_id);
            footList.remove(positon);
            notifyItemRangeChanged(listData.size()+headList.size(),getItemCount());
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


    public void removeT(int position)
    {
        listData.remove(position);
        notifyItemRemoved(position+listData.size());
    }
}

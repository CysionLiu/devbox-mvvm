package com.ywc.recycler.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ywc.recycler.holder.BaseViewHold;
import com.ywc.recycler.mode.LoadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHold>{
    private List<T> listData;
    private int itemLayout;
    private Context context;
    private Activity activity;
    public Activity getActivity() {
        return activity;
    }

    public List<T> getListData() {
        return listData;
    }

    public Context getContext() {
        return context;
    }

    public BaseAdapter(Context context, int itemLayout, List<T> listData) {
        this(context,itemLayout);
        this.listData.addAll(listData);
    }

    public BaseAdapter( Context context,int itemLayout) {
        this(context);
        this.itemLayout = itemLayout;
    }

    public BaseAdapter(Context context)
    {
        this.context = context;
        this.listData=new ArrayList<>();
        if (context instanceof Activity)
            activity = ((Activity) context);
    }


    public int getHeadCount()
    {
        return 0;
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
        T t = listData.get(position-getHeadCount());
        onCustomHolder(holder,position-getHeadCount(),t);
        itemListener(holder,position-getHeadCount(),t);
    }

    protected abstract void onCustomHolder(BaseViewHold holder,int position, T t);
    protected void itemListener(BaseViewHold hold,int position,T t)
    {

    }

    //设置动画
    private boolean is_Anima;

    public void flush(List<T> list)
    {
        if (listData.size()>0)
            listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void flushT(int position)
    {
        if (position<getItemCount())
            notifyItemChanged(position+getHeadCount());
    }

    public void removeT(int position)
    {
        listData.remove(position);
        notifyItemRemoved(position+getHeadCount());
        notifyItemRangeChanged(position+getHeadCount(),getItemCount());
    }

    public void addAll(List<T> list)
    {
        int startPosition = listData.size();
        listData.addAll(list);
        notifyItemRangeChanged(startPosition+getHeadCount(),getItemCount());
    }

    public void addT(T t)
    {
        int startPosition = listData.size();
        listData.add(t);
        notifyItemRangeChanged(startPosition+getHeadCount(),getItemCount());
    }

    public void clear()
    {
        if (listData.size()>0)
        {
            listData.clear();
            notifyDataSetChanged();
        }
    }


    public void flushOrAdd(List<T> list, int loadMode)
    {
        if (loadMode==LoadMode.PULL_UP)
            addAll(list);
        else
            flush(list);
    }

    public void flush()
    {
        notifyDataSetChanged();
    }

    public T obtainT(int position)
    {
        return getListData().get(position);
    }

    //字符串存在数据
    protected boolean is_String(String content)
    {
        if (content==null||content.equals(""))
            return false;
        return true;
    }

    protected String inputString(String content)
    {
        if (content==null)
            return "";
        return content;
    }

    protected String inputNum(String content)
    {
        if (content==null||content.equals(""))
            return "0";
        return content;
    }

    private String type_content[]={"分享","回复","赞"};
    protected String inputNum(int num,int type)
    {
        if (num<=0)
        {
            if (type<=2)
            {
                return type_content[type];
            }
        }
        return num+"";
    }

    protected String inputImage(String content)
    {
        if (is_String(content))
            return content;
        return "【分享图片】";
    }

}

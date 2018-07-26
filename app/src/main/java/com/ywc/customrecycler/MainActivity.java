package com.ywc.customrecycler;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.ywc.recycler.RecyclerLayout;
import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.holder.BaseViewHold;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.ScrollMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerLayout reycler;
    private Adapter adapter;
    private View head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        reycler = ((RecyclerLayout) findViewById(R.id.reycler));
        head = LayoutInflater.from(this).inflate(R.layout.head,reycler,false);
        adapter = new Adapter(new ArrayList<String>(),R.layout.adapter,this);
        reycler.with(ScrollMode.BOTH, new LinearLayoutManager(this), adapter, new OnScrollCall() {
            @Override
            public void callback(ScrollMode scrollMode) {

            }
        });

        List<String> listData=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listData.add("不错的人");
        }
        adapter.addAll(listData);
        reycler.setScrollMode(ScrollMode.BOTH);
        reycler.addFoot(R.layout.head);
        reycler.addHead(head);
        reycler.onScrollFinish();
    }


    class Adapter extends CustomAdapter<String>
    {

        public Adapter(List<String> listData, int itemLayout, Context context) {
            super(listData, itemLayout, context);
        }

        @Override
        protected void fdById(BaseViewHold holder, int position, String s) {

        }

        @Override
        protected void fillData(BaseViewHold holder, int position, String s) {

        }
    }
}

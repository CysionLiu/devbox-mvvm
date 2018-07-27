package com.ywc.customrecycler;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.ywc.recycler.CustomRecycler;
import com.ywc.recycler.CustomScrollView;
import com.ywc.recycler.RecyclerLayout;
import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.holder.BaseViewHold;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.ScrollMode;
import com.ywc.recycler.scroll.CustomScroll;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CustomRecycler customScroll;
    private Adapter adapter;
    private CustomScrollView customScrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customScrollLayout = ((CustomScrollView) findViewById(R.id.customScrollLayout));



        customScroll = ((CustomRecycler) findViewById(R.id.customScroll));
        adapter = new Adapter(new ArrayList<String>(),R.layout.adapter,this);

        customScroll.setScollMode(ScrollMode.BOTH);
//        customScroll.setNestedScrollingEnabled(false);
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("bucuo");
        }

        customScroll.setLayoutManager(new LinearLayoutManager(this));
        customScroll.setAdapter(adapter);
        adapter.addAll(list);

        customScrollLayout.init(customScroll, new OnScrollCall() {
            @Override
            public void callback(ScrollMode scrollMode) {

            }
        });
        customScrollLayout.setScrollMode(ScrollMode.BOTH);
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

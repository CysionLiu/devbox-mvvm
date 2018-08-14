package com.ywc.customrecycler;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;


import com.ywc.recycler.CustomRecycler;
import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.holder.BaseViewHold;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.LoadMode;
import com.ywc.recycler.mode.ScrollMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private CustomRecycler recycler;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = ((CustomRecycler) findViewById(R.id.recycler));

        adapter = new Adapter(this,R.layout.adapter,new ArrayList<String>());
        recycler.with( ScrollMode.BOTH,new LinearLayoutManager(this), adapter, new OnScrollCall() {
            @Override
            public void callback(LoadMode loadMode) {

            }
        });
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("不错呀");
        }
        adapter.addAll(list);
        recycler.setScollMode(ScrollMode.BOTH);
    }

    class Adapter extends CustomAdapter<String>
    {


        public Adapter(Context context, int itemLayout, List<String> listData) {
            super(context, itemLayout, listData);
        }

        @Override
        protected void onCustomHolder(BaseViewHold holder, int position, String s) {

        }


    }
}

package com.ywc.customrecycler;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;


import com.ywc.recycler.CustomRecycler;
import com.ywc.recycler.RecyclerLayout;
import com.ywc.recycler.adapter.CustomAdapter;
import com.ywc.recycler.holder.BaseViewHold;
import com.ywc.recycler.io.OnScrollCall;
import com.ywc.recycler.mode.LoadMode;
import com.ywc.recycler.mode.ScrollMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerLayout recycler;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recycler);
        recycler.setScrollMode(ScrollMode.BOTH);
        adapter=new Adapter(this,R.layout.adapter);
        recycler.with(ScrollMode.NULL, new LinearLayoutManager(this), adapter, new OnScrollCall() {
            @Override
            public void callback(LoadMode loadMode) {

            }
        });
        View inflate = LayoutInflater.from(this).inflate(R.layout.adapter, recycler,false);
        recycler.onEndHandler(0,LoadMode.PULL_DOWN,inflate);
    }

    class Adapter extends CustomAdapter<String>
    {


        public Adapter(Context context, int itemLayout) {
            super(context, itemLayout);
        }

        @Override
        protected void onCustomHolder(BaseViewHold holder, int position, String s) {

        }


    }
}

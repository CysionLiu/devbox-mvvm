package com.ywc.customrecycler;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

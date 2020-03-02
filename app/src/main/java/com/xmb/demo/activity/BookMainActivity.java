package com.xmb.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xmb.demo.R;
import com.xmb.demo.adapter.BookRecycleAdapter;
import com.xmb.demo.listener.BookRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by Ben
 * On 2020-03-02.
 *
 * @Descption
 */
public class BookMainActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_main_recycleview);
        recyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        List<String> dataList = new ArrayList<>();
        final int ChapterTotal = 1098;
        for (int i = 0; i < 1098; i++) {
            dataList.add(i + "");
        }

        // specify an adapter (see also next example)
        mAdapter = new BookRecycleAdapter(dataList, new BookRecycleViewItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(BookMainActivity.this, BookDetailActivity.class);
                intent.putExtra("chapterNum", position);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

    }
}

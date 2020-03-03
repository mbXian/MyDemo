package com.xmb.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.xmb.demo.R;
import com.xmb.demo.adapter.BookRecycleAdapter;
import com.xmb.demo.listener.BookRecycleViewItemClickListener;
import com.xmb.demo.network.MyCallBack;
import com.xmb.demo.network.NetClient;
import com.xmb.demo.network.NetWorkUrl;
import com.xmb.demo.utils.BookSharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
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
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_main_recycleview);
        recyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        titleTextView = (TextView) findViewById(R.id.book_title_text_view);

        titleTextView.setText("《上门龙婿免费全文阅读》\n" +
                "作者:叶辰");

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final List<String> dataList = new ArrayList<>();

        // specify an adapter (see also next example)
        mAdapter = new BookRecycleAdapter(dataList, new BookRecycleViewItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(BookMainActivity.this, BookDetailActivity.class);
                intent.putExtra("chapterNum", position + 1);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        NetClient.getNetClient().callNetPost(NetWorkUrl.Book_count_Url, new JSONObject(), new MyCallBack() {
            @Override
            public void onFailure(int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final String json) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            final Long totalChapter = jsonObject.getLong("data");
                            if (totalChapter != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < totalChapter; i++) {
                                            dataList.add(i + "");
                                        }
                                        mAdapter.notifyDataSetChanged();

                                        int chapterNum = BookSharedPreferencesUtils.instants(BookMainActivity.this).getChapterNum();
                                        System.out.println("chapterNum = " + chapterNum);
                                        recyclerView.scrollToPosition(chapterNum);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BookMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BookMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}

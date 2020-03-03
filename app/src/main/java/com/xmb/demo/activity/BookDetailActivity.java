package com.xmb.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xmb.demo.R;
import com.xmb.demo.adapter.BookRecycleAdapter;
import com.xmb.demo.network.MyCallBack;
import com.xmb.demo.network.NetClient;
import com.xmb.demo.network.NetWorkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Author by Ben
 * On 2020-03-02.
 *
 * @Descption
 */
public class BookDetailActivity extends Activity {
    private EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_view);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int chapterNum = bundle.getInt("chapterNum");

        textView = (EditText) findViewById(R.id.book_detail_textView);
        textView.setText("请求中，请耐心等待...");
        getContent(chapterNum);
    }

    private void getContent(int chapterNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("chapterNum", chapterNum);
        } catch (Exception e) {

        }

        NetClient.getNetClient().callNetPost(NetWorkUrl.Book_detail_Url, jsonObject, new MyCallBack() {
            @Override
            public void onFailure(int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("请求失败，请重试");
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (data != null) {
                                final String content = data.getString("content");
                                if (content != null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView.setText(content);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView.setText("请求失败，请重试");
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText("请求失败，请重试");
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("请求失败，请重试");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("请求失败，请重试");
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("请求失败，请重试");
                        }
                    });
                }
            }
        });
    }
}

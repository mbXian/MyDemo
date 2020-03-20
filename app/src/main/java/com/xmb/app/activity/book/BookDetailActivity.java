package com.xmb.app.activity.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.xmb.app.R;
import com.xmb.app.utils.book.BookSharedPreferencesUtils;
import com.xmb.app.network.MyCallBack;
import com.xmb.app.network.NetClient;
import com.xmb.app.network.NetWorkUrl;

import org.json.JSONException;
import org.json.JSONObject;

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
        final int chapterNumSave = chapterNum;

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

                                            BookSharedPreferencesUtils.instants(BookDetailActivity.this).saveChapterNum(chapterNumSave - 1);
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

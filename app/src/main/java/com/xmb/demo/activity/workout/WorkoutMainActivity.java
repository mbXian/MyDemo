package com.xmb.demo.activity.workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.xmb.demo.R;
import com.xmb.demo.network.MyCallBack;
import com.xmb.demo.network.NetClient;
import com.xmb.demo.network.NetWorkUrl;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author by Ben
 * On 2020-03-07.
 *
 * @Descption
 */
public class WorkoutMainActivity extends Activity {

    private Button uploadButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_main);

        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NetClient.getNetClient().callNetPost(NetWorkUrl.WORKOUT_DAILY_DATA_UPLOAD_TEMP_URL, new JSONObject(), new MyCallBack() {
                    @Override
                    public void onFailure(int code) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WorkoutMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WorkoutMainActivity.this, "Upload Success! Keep it!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WorkoutMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(WorkoutMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(WorkoutMainActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });
    }
}

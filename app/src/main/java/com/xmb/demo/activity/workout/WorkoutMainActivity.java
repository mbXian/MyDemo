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

import java.util.Date;

/**
 * Author by Ben
 * On 2020-03-07.
 *
 * @Descption
 */
public class WorkoutMainActivity extends Activity {

    private Button uploadButton;
    private Button startButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_main);

        final JSONObject paramJsonObject = new JSONObject();

        uploadButton = findViewById(R.id.uploadButton);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    paramJsonObject.put("trainTimeMilliSec", new Date().getTime());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startButton.setVisibility(View.GONE);
                            uploadButton.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {

                }
            }
        });

        uploadButton.setVisibility(View.GONE);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            NetClient.getNetClient().callNetPost(NetWorkUrl.WORKOUT_DAILY_DATA_UPLOAD_TEMP_URL, paramJsonObject, new MyCallBack() {
                @Override
                public void onFailure(int code) {
                    showErrorTips("请求失败，请重试");
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
                                        uploadButton.setEnabled(false);
                                        Toast.makeText(WorkoutMainActivity.this, "Upload Success! Keep it!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                showErrorTips("请求失败，请重试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showErrorTips("请求失败，请重试");
                        }
                    } else {
                        showErrorTips("请求失败，请重试");
                    }
                }
            });
            }
        });
    }

    private void showErrorTips(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WorkoutMainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}

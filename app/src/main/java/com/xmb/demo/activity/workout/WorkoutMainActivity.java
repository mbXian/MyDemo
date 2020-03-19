package com.xmb.demo.activity.workout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xmb.demo.R;
import com.xmb.demo.network.MyCallBack;
import com.xmb.demo.network.NetClient;
import com.xmb.demo.network.NetWorkUrl;

import org.json.JSONArray;
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
    private Button refreshButton;
    private EditText statisticsEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_main);

        final JSONObject paramJsonObject = new JSONObject();

        uploadButton = findViewById(R.id.uploadButton);
        startButton = findViewById(R.id.startButton);
        refreshButton = findViewById(R.id.refreshButton);
        statisticsEditText = findViewById(R.id.statisticsEditText);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStatisticsData();
            }
        });

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

        //密码确认输入框
        final EditText uploadPasswordEditText = new EditText(WorkoutMainActivity.this);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WorkoutMainActivity.this);
        dialogBuilder.setTitle("Upload Password");
        dialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        //设置dialog布局
        dialogBuilder.setView(uploadPasswordEditText);
        //设置按钮
        dialogBuilder.setPositiveButton("Confirm"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (TextUtils.isEmpty(uploadPasswordEditText.getText())) {
                            showErrorTips("请填写密码");
                            return;
                        }

                        try {
                            paramJsonObject.put("password", uploadPasswordEditText.getText());
                        } catch (Exception e) {

                        }

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
                                                    Toast.makeText(WorkoutMainActivity.this, "Upload Success! Keep it!", Toast.LENGTH_LONG).show();
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
                }).setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = dialogBuilder.create();

        uploadButton.setVisibility(View.GONE);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPasswordEditText.setText("");
                alertDialog.show();
            }
        });

        requestStatisticsData();
    }

    private void requestStatisticsData() {
        final StringBuilder statisticsDataStringBuilderToday = new StringBuilder();
        statisticsDataStringBuilderToday.append("今日数据统计：\n");

        final StringBuilder statisticsDataStringBuilderTonow = new StringBuilder();
        statisticsDataStringBuilderTonow.append("至今数据统计：\n");

        NetClient.getNetClient().callNetPost(NetWorkUrl.WORKOUT_TODAY_STATISTICS_URL, new JSONObject(), new MyCallBack() {
            @Override
            public void onFailure(int code) {
                statisticsDataStringBuilderToday.append("获取失败！");

                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }

            @Override
            public void onResponse(String json) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            if (jsonObjectData != null) {
                                JSONArray jsonArray = jsonObjectData.getJSONArray("statisticsEachTypeVOList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject vo = (JSONObject)jsonArray.get(i);
                                    statisticsDataStringBuilderToday.append(vo.getString("nameCN") + ": " + vo.getLong("countTotal") + "\n");
                                }
                            }
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                } else {

                }
                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }
        });

        NetClient.getNetClient().callNetPost(NetWorkUrl.WORKOUT_TONOW_STATISTICS_URL, new JSONObject(), new MyCallBack() {
            @Override
            public void onFailure(int code) {
                statisticsDataStringBuilderTonow.append("获取失败！");

                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }

            @Override
            public void onResponse(String json) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            if (jsonObjectData != null) {
                                JSONArray jsonArray = jsonObjectData.getJSONArray("statisticsEachTypeVOList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject vo = (JSONObject)jsonArray.get(i);
                                    statisticsDataStringBuilderTonow.append(vo.getString("nameCN") + ": " + vo.getLong("countTotal") + "\n");
                                }
                            }
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                } else {

                }
                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }
        });
    }

    private void showStatisticsData(final StringBuilder statisticsDataStringBuilderToday, final StringBuilder statisticsDataStringBuilderTonow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statisticsEditText.setText(statisticsDataStringBuilderToday.toString() + "\n\n" + statisticsDataStringBuilderTonow.toString());
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

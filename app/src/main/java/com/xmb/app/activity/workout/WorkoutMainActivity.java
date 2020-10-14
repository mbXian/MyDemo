package com.xmb.app.activity.workout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xmb.app.R;
import com.xmb.app.network.MyCallBack;
import com.xmb.app.network.NetClient;
import com.xmb.app.network.NetWorkUrl;
import com.xmb.app.utils.XDateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    private Date startDate;

    private Timer timer;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_main);

        final JSONObject paramJsonObject = new JSONObject();

        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setText("Upload");
        startButton = findViewById(R.id.startButton);
        refreshButton = findViewById(R.id.refreshButton);
        statisticsEditText = findViewById(R.id.statisticsEditText);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStatisticsData();
            }
        });

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //声明定位回调监听器
        AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                latitude = BigDecimal.valueOf(amapLocation.getLatitude());
                longitude = BigDecimal.valueOf(amapLocation.getLongitude());
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(option);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        mLocationClient.startLocation();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startDate = new Date();

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    uploadButton.setText(XDateUtils.parseTimeString(System.currentTimeMillis() - startDate.getTime()));
                                }
                            });

                        }
                    }, 1000, 1000);

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
                            paramJsonObject.put("trainTimeMilliSec", startDate.getTime());
                            paramJsonObject.put("finishTimeMilliSec", new Date().getTime());
                            paramJsonObject.put("password", uploadPasswordEditText.getText());
                            if (latitude != null) {
                                paramJsonObject.put("latitude", latitude);
                            }
                            if (longitude != null) {
                                paramJsonObject.put("longitude", longitude);
                            }
                        } catch (Exception e) {

                        }

//                        Log.i("xmb", "paramJsonObject = " + paramJsonObject.toString());
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
                                        if (code == 0 || code == 200) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startButton.setVisibility(View.VISIBLE);
                                                    uploadButton.setVisibility(View.GONE);
                                                    uploadButton.setText("Upload");

                                                    timer.cancel();
                                                    timer = null;

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
        statisticsDataStringBuilderToday.append("🏆 Statistics Today（今日数据统计）：\n\n");

        final StringBuilder statisticsDataStringBuilderTonow = new StringBuilder();
        statisticsDataStringBuilderTonow.append("🏆 Statistics So Far（至今数据统计）：\n\n");

        NetClient.getNetClient().callNetPost(NetWorkUrl.WORKOUT_TODAY_STATISTICS_URL, new JSONObject(), new MyCallBack() {
            @Override
            public void onFailure(int code) {
                statisticsDataStringBuilderToday.append("＊ 获取失败！\n\n");

                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }

            @Override
            public void onResponse(String json) {

                boolean requestDataSuccess = false;

                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 0 || code == 200) {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            if (jsonObjectData != null) {
                                Long duration = jsonObjectData.getLong("duration");
                                JSONArray jsonArray = jsonObjectData.getJSONArray("statisticsEachTypeVOList");

                                if (jsonArray.length() == 0 || duration == null || duration == 0) {
                                    statisticsDataStringBuilderToday.append("＊ You have not train today. Come on!\n\n");
                                } else {
                                    statisticsDataStringBuilderToday.append("＊ Duration（耗时）：" + XDateUtils.parseTimeString(duration * 1000) + "\n\n");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject vo = (JSONObject)jsonArray.get(i);
                                        statisticsDataStringBuilderToday.append("＊ " + vo.getString("name") + "（" + vo.getString("nameCN") + "）: " + vo.getLong("countTotal") + "\n\n");
                                    }
                                }

                                requestDataSuccess = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                if (!requestDataSuccess) {
                    statisticsDataStringBuilderToday.append("＊ 获取失败！\n\n");
                }
                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }
        });

        NetClient.getNetClient().callNetPost(NetWorkUrl.WORKOUT_TONOW_STATISTICS_URL, new JSONObject(), new MyCallBack() {
            @Override
            public void onFailure(int code) {
                statisticsDataStringBuilderTonow.append("＊ 获取失败！\n\n");

                showStatisticsData(statisticsDataStringBuilderToday, statisticsDataStringBuilderTonow);
            }

            @Override
            public void onResponse(String json) {

                boolean requestDataSuccess = false;

                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 0 || code == 200) {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            if (jsonObjectData != null) {
                                Long duration = jsonObjectData.getLong("duration");
                                Long startTrainTime = jsonObjectData.getLong("startTrainTime");
                                Long endTrainTime = jsonObjectData.getLong("endTrainTime");
                                Long times = jsonObjectData.getLong("times");

                                JSONArray jsonArray = jsonObjectData.getJSONArray("statisticsEachTypeVOList");

                                if (jsonArray.length() == 0 || duration == null || duration == 0) {
                                    statisticsDataStringBuilderTonow.append("＊ You have not train so far. Come on!\n\n");
                                } else {
                                    statisticsDataStringBuilderTonow.append("＊ From " + XDateUtils.format(new Date(startTrainTime), XDateUtils.DATE_PATTERN) + " to " + XDateUtils.format(new Date(endTrainTime), XDateUtils.DATE_PATTERN) + "\n\n");
                                    statisticsDataStringBuilderTonow.append("＊ Total train times（共锻炼次数）：" + times + "\n\n");
                                    statisticsDataStringBuilderTonow.append("＊ Duration（耗时）：" + XDateUtils.parseTimeString(duration * 1000) + "\n\n");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject vo = (JSONObject)jsonArray.get(i);
                                        statisticsDataStringBuilderTonow.append("＊ " + vo.getString("name") + "（" + vo.getString("nameCN") + "）: " + vo.getLong("countTotal") + "\n\n");
                                    }
                                }

                                requestDataSuccess = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                if (!requestDataSuccess) {
                    statisticsDataStringBuilderTonow.append("＊ 获取失败！\n\n");
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

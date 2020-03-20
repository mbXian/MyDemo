package com.xmb.app.activity.meal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.xmb.app.R;
import com.xmb.app.entity.meal.RecommendMeal;
import com.xmb.app.network.MyCallBack;
import com.xmb.app.network.NetClient;
import com.xmb.app.network.NetWorkUrl;

import org.json.JSONException;
import org.json.JSONObject;

public class MealActivity extends AppCompatActivity {
    private static final String TAG = "MealActivity";
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
        contentTextView.setText("获取不到内容!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "开始请求........................");
        requestRecommendMeal();
    }

    //请求推荐餐饮
    private void requestRecommendMeal() {
        NetClient.getNetClient().callNetGet(NetWorkUrl.Recommend_Meal_Url, new MyCallBack() {
            @Override
            public void onFailure(int code) {
                //依据响应码判断下一步操作
                Log.i("a", "fail code = " + code);
            }

            @Override
            public void onResponse(String json) {
                //使用传过来的json数据进行下一步操作
                Log.i("a", "success result = " + json);
                final RecommendMeal recommendMeal = new RecommendMeal();
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject dataJSONObject = jsonObject.getJSONObject("data");
                            if (dataJSONObject != null) {
                                if (!isDataEmpty(dataJSONObject.getString("soupBisque"))) {
                                    recommendMeal.setSoupBisque(dataJSONObject.getString("soupBisque"));
                                }
                                if (!isDataEmpty(dataJSONObject.getString("soupBroth"))) {
                                    recommendMeal.setSoupBroth(dataJSONObject.getString("soupBroth"));
                                }
                                if (!isDataEmpty(dataJSONObject.getString("vegetablesScrambledMeat"))) {
                                    recommendMeal.setVegetablesScrambledMeat(dataJSONObject.getString("vegetablesScrambledMeat"));
                                }
                                if (!isDataEmpty(dataJSONObject.getString("meat"))) {
                                    recommendMeal.setMeat(dataJSONObject.getString("meat"));
                                }
                                if (!isDataEmpty(dataJSONObject.getString("vegetables"))) {
                                    recommendMeal.setVegetables(dataJSONObject.getString("vegetables"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, recommendMeal.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentTextView.setText(recommendMeal.parseShowContent());
                    }
                });
            }
        });
    }

    //判断返回的数据是否为空
    private boolean isDataEmpty(String data) {
        return TextUtils.isEmpty(data) || data.equals("null");
    }
}

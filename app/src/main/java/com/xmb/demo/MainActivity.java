package com.xmb.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xmb.demo.entity.RecommendMeal;
import com.xmb.demo.network.MyCallBack;
import com.xmb.demo.network.NetClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        webView = (WebView) findViewById(R.id.webView);

        String url = "http://192.168.0.108:8090/meal/recommend";
//        String url = "https://www.baidu.com";
        NetClient.getNetClient().callNetGet(url, new MyCallBack() {
            @Override
            public void onFailure(int code) {
                //依据响应码判断下一步操作
                Log.i("a", "fail code = " + code);
            }

            @Override
            public void onResponse(String json) {
                //使用传过来的json数据进行下一步操作
                Log.i("a", "success result = " + json);
                RecommendMeal recommendMeal = new RecommendMeal();
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject dataJSONObject = jsonObject.getJSONObject("data");
                            if (dataJSONObject != null) {
                                if (!TextUtils.isEmpty(dataJSONObject.getString("soupBisque"))) {
                                    recommendMeal.setSoupBisque(dataJSONObject.getString("soupBisque"));
                                }
                                if (!TextUtils.isEmpty(dataJSONObject.getString("soupBroth"))) {
                                    recommendMeal.setSoupBroth(dataJSONObject.getString("soupBroth"));
                                }
                                if (!TextUtils.isEmpty(dataJSONObject.getString("vegetablesScrambledMeat"))) {
                                    recommendMeal.setVegetablesScrambledMeat(dataJSONObject.getString("vegetablesScrambledMeat"));
                                }
                                if (!TextUtils.isEmpty(dataJSONObject.getString("meat"))) {
                                    recommendMeal.setMeat(dataJSONObject.getString("meat"));
                                }
                                if (!TextUtils.isEmpty(dataJSONObject.getString("vegetables"))) {
                                    recommendMeal.setVegetables(dataJSONObject.getString("vegetables"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, recommendMeal.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

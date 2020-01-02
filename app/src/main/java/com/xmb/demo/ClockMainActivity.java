package com.xmb.demo;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author by Ben
 * On 2019-12-29.
 *
 * @Descption
 */
public class ClockMainActivity extends AppCompatActivity {

    final static String TAG = "ClockMainActivity";

    private TextView hourTextView1;
    private TextView hourTextView2;
    private TextView minuteTextView1;
    private TextView minuteTextView2;
    private TextView secondTextView1;
    private TextView secondTextView2;
    private TextView dateTextView;
    private TextView weekTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_clock);

        hourTextView1 = (TextView) findViewById(R.id.hourTextView1);
        hourTextView2 = (TextView) findViewById(R.id.hourTextView2);
        minuteTextView1 = (TextView) findViewById(R.id.minuteTextView1);
        minuteTextView2 = (TextView) findViewById(R.id.minuteTextView2);
        secondTextView1 = (TextView) findViewById(R.id.secondTextView1);
        secondTextView2 = (TextView) findViewById(R.id.secondTextView2);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        weekTextView = (TextView) findViewById(R.id.weekTextView);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");

        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Date currentDate = new Date();

                        String formatString = simpleDateFormat.format(currentDate);
                        String hour1 = formatString.substring(11, 12);
                        hourTextView1.setText(hour1);
                        String hour2 = formatString.substring(12, 13);
                        hourTextView2.setText(hour2);
                        String minute1 = formatString.substring(14, 15);
                        minuteTextView1.setText(minute1);
                        String minute2 = formatString.substring(15, 16);
                        minuteTextView2.setText(minute2);
                        String second1 = formatString.substring(17, 18);
                        secondTextView1.setText(second1);
                        String second2 = formatString.substring(18, 19);
                        secondTextView2.setText(second2);

                        String dateString = formatString.substring(0, 10);
                        dateTextView.setText(dateString);

                        String weekString = weekFormat.format(currentDate);
                        weekTextView.setText(weekString);
                    }
                });

            }
        }, 100, 150);
    }
}

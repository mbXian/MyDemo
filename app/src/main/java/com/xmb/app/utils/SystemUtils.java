package com.xmb.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Author by Ben
 * On 2020-01-03.
 *
 * @Descption
 */
public class SystemUtils {

    private final static String TAG = "SystemUtils";

    /**
     *  实时获取电量
     */
    public static int getSystemBattery(Context context){
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryInfoIntent.getIntExtra("level",0);
        int batterySum = batteryInfoIntent.getIntExtra("scale", 100);
        int percentBattery = 100 *  level / batterySum;

        return percentBattery;
    }
}

package com.xmb.app.utils;

import com.xmb.app.entity.ParseTimeDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author by Ben
 * On 2020-03-20.
 *
 * @Descption
 */
public class XDateUtils {

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式(yyyyMMddHHmmss)
     */
    public final static String DATE_TIME_PATTERN_WITHOUT_SYMBOL = "yyyyMMddHHmmss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * @param date 日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 获取一天的开始时间
     * @param date
     * @return
     */
    public static Date getDayStartTime(Date date) {
        String dateFormat = format(date, DATE_TIME_PATTERN);
        dateFormat = dateFormat.substring(0, 11) + "00:00:00";
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            date = df.parse(dateFormat);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 获取一天的结束时间
     * @param date
     * @return
     */
    public static Date getDayEndTime(Date date) {
        String dateFormat = format(date, DATE_TIME_PATTERN);
        dateFormat = dateFormat.substring(0, 11) + "23:59:59";
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            date = df.parse(dateFormat);
        } catch (Exception e) {

        }
        return date;
    }

    public static int MILLISECONDS_A_SECOND = 1000;
    public static int SECONDS_A_MINUTE = 60;
    public static int MINUTES_A_HOUR = 60;
    public static int HOURS_A_DAY = 24;
    public static int DAYS_A_WEEK = 7;
    public static int WEEKS_A_MONTH = 4;
    public static int DAYS_A_MONTH = 30;
    public static int MONTHES_A_YEAR = 12;

    public static ParseTimeDTO calculateAndParseTime(long time) {
        ParseTimeDTO dto = new ParseTimeDTO();
        if (time <= 0) {
            return dto;
        }

        dto.setMilliSeconds(time % MILLISECONDS_A_SECOND);
        time = (time - dto.getMilliSeconds()) / MILLISECONDS_A_SECOND;

        dto.setSeconds(time % SECONDS_A_MINUTE);
        time = (time - dto.getSeconds()) / SECONDS_A_MINUTE;

        dto.setMinutes(time % MINUTES_A_HOUR);
        time = (time - dto.getMinutes()) / MINUTES_A_HOUR;

        dto.setHours(time % HOURS_A_DAY);
        time = (time - dto.getHours()) / HOURS_A_DAY;

        dto.setWeeks(time % DAYS_A_WEEK);

        dto.setMonthes(time % DAYS_A_MONTH);
        time = (time - dto.getMonthes()) / DAYS_A_MONTH;

        dto.setYears(time % MONTHES_A_YEAR);

        return dto;
    }

    public static String parseTimeIfLowThenTen(long number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return number + "";
        }
    }

    public static String parseTimeString(long time) {
        StringBuilder stringBuilder = new StringBuilder();
        ParseTimeDTO parseTimeDTO = XDateUtils.calculateAndParseTime(time);
        if (parseTimeDTO.getYears() > 0) {
            stringBuilder.append(XDateUtils.parseTimeIfLowThenTen(parseTimeDTO.getYears())).append("年");
        }
        if (parseTimeDTO.getMonthes() > 0) {
            stringBuilder.append(XDateUtils.parseTimeIfLowThenTen(parseTimeDTO.getMonthes())).append("月");
        }
        if (parseTimeDTO.getDays() > 0) {
            stringBuilder.append(XDateUtils.parseTimeIfLowThenTen(parseTimeDTO.getDays())).append("日");
        }
        if (parseTimeDTO.getHours() > 0) {
            stringBuilder.append(XDateUtils.parseTimeIfLowThenTen(parseTimeDTO.getHours())).append("时");
        }
        if (parseTimeDTO.getMinutes() > 0) {
            stringBuilder.append(XDateUtils.parseTimeIfLowThenTen(parseTimeDTO.getMinutes())).append("分");
        }
        if (parseTimeDTO.getSeconds() > 0) {
            stringBuilder.append(XDateUtils.parseTimeIfLowThenTen(parseTimeDTO.getSeconds())).append("秒");
        }
        return stringBuilder.toString();
    }
}

package com.xmb.demo.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
     * LocalDateTime to Date
     * @param localDateTime
     * @return
     */
    public static Date convertFromLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date to LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime convertFromDateToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
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
}

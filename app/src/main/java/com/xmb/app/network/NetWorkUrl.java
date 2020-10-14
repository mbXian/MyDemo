package com.xmb.app.network;

/**
 * Author by Ben
 * On 2019-09-05.
 *
 * @Descption
 */
public class NetWorkUrl {
    //服务器IP
//    public static final String Server_IP = "http://xian.seaven.cn:8090";
    public static final String Server_IP = "http://111.229.74.28:8091";
//    public static final String Server_IP = "http://10.10.115.30:8091";

    //推荐餐饮
    public static final String Recommend_Meal_Url = "/meal/recommend";

    //读书
    public static final String Book_info_Url = "/book/getBookInfo";
    public static final String Book_detail_Url = "/book/getContent";
    public static final String Book_count_Url = "/book/getTotalChapter";

    //锻炼
    public static final String WORKOUT_DAILY_DATA_UPLOAD_TEMP_URL = "/workout/workoutrecord/enterDailyDataTemporary";
    public static final String WORKOUT_TODAY_STATISTICS_URL = "/workout/workoutrecord/todayStatistics";
    public static final String WORKOUT_TONOW_STATISTICS_URL = "/workout/workoutrecord/toNowStatistics";
}

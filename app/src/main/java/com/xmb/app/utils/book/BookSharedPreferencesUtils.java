package com.xmb.app.utils.book;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author by Ben
 * On 2020-03-03.
 *
 * @Descption
 */
public class BookSharedPreferencesUtils {
    private static BookSharedPreferencesUtils utils = null;
    private SharedPreferences prefs = null;

    public static BookSharedPreferencesUtils instants(Context context) {
        if (utils == null) {
            utils = new BookSharedPreferencesUtils();
            utils.prefs = context.getSharedPreferences(
                    "com.example.app", Context.MODE_PRIVATE);
        }
        return utils;
    }


    public void saveChapterNum(int chapterNum) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ChapterNum", chapterNum);
        editor.apply();
    }

    public int getChapterNum() {
        int chapterNum = prefs.getInt("ChapterNum", 0);
        return chapterNum;
    }
}

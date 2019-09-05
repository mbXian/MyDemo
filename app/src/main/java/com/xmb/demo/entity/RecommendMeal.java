package com.xmb.demo.entity;

import android.text.TextUtils;

/**
 * Author by Ben
 * On 2019-09-05.
 *
 * @Descption
 */
public class RecommendMeal {
    //老火汤
    private String soupBisque;
    //例汤
    private String soupBroth;
    //菜炒肉
    private String vegetablesScrambledMeat;
    //肉
    private String meat;
    //青菜
    private String vegetables;

    public String parseShowContent() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(soupBisque)) {
            stringBuilder.append("老火汤：").append(soupBisque).append("\n\n");
        }
        if (!TextUtils.isEmpty(soupBroth)) {
            stringBuilder.append("例汤：").append(soupBroth).append("\n\n");
        }
        if (!TextUtils.isEmpty(vegetablesScrambledMeat)) {
            stringBuilder.append("菜炒肉：").append(vegetablesScrambledMeat).append("\n\n");
        }
        if (!TextUtils.isEmpty(meat)) {
            stringBuilder.append("肉：").append(meat).append("\n\n");
        }
        if (!TextUtils.isEmpty(vegetables)) {
            stringBuilder.append("青菜：").append(vegetables).append("\n\n");
        }
        return stringBuilder.toString();
    }

    public String getSoupBisque() {
        return soupBisque;
    }

    public void setSoupBisque(String soupBisque) {
        this.soupBisque = soupBisque;
    }

    public String getSoupBroth() {
        return soupBroth;
    }

    public void setSoupBroth(String soupBroth) {
        this.soupBroth = soupBroth;
    }

    public String getVegetablesScrambledMeat() {
        return vegetablesScrambledMeat;
    }

    public void setVegetablesScrambledMeat(String vegetablesScrambledMeat) {
        this.vegetablesScrambledMeat = vegetablesScrambledMeat;
    }

    public String getMeat() {
        return meat;
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }

    public String getVegetables() {
        return vegetables;
    }

    public void setVegetables(String vegetables) {
        this.vegetables = vegetables;
    }
}

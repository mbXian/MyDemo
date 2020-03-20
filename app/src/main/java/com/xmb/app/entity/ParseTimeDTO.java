package com.xmb.app.entity;

import android.content.Intent;

/**
 * Author by Ben
 * On 2020-03-20.
 *
 * @Descption
 */
public class ParseTimeDTO {
    private long years;
    private long monthes;
    private long weeks;
    private long days;
    private long hours;
    private long minutes;
    private long seconds;
    private long milliSeconds;

    public long getYears() {
        return years;
    }

    public void setYears(long years) {
        this.years = years;
    }

    public long getMonthes() {
        return monthes;
    }

    public void setMonthes(long monthes) {
        this.monthes = monthes;
    }

    public long getWeeks() {
        return weeks;
    }

    public void setWeeks(long weeks) {
        this.weeks = weeks;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getMilliSeconds() {
        return milliSeconds;
    }

    public void setMilliSeconds(long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }
}

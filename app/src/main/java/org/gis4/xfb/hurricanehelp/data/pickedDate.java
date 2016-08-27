package org.gis4.xfb.hurricanehelp.data;

import java.util.Date;

/**
 * 选择好的日期
 * Created by chi on 2016-8-12.
 */
public class pickedDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public pickedDate(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String toString(){
        return
                String.valueOf(this.year)+"年"+
                String.valueOf(this.month)+"月"+
                String.valueOf(this.day)+"日"+
                String.valueOf(this.hour)+"点"+
                String.valueOf(this.minute)+"分";
    }

    public boolean earlierThan(pickedDate tempDate) {
        if(tempDate.getYear() > this.year) return true;
        if(tempDate.getMonth() > this.month) return true;
        if(tempDate.getDay() > this.day) return true;
        if(tempDate.getHour() > this.hour) return true;
        if(tempDate.getMinute() > this.minute) return true;

        return false;
    }

    public Date getDate(){
        return new Date(year, month, day, hour, minute);
    }
}

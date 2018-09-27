package com.moonface.schoolix;

import android.support.annotation.NonNull;
import java.util.Comparator;

public class Class{
    private Integer day;
    private Integer hour;
    private Subject subject;

    @interface Days{}
    public static final int Monday = 0;
    public static final int Tuesday = 1;
    public static final int Wednesday = 2;
    public static final int Thursday = 3;
    public static final int Friday = 4;
    public static final int Saturday = 5;
    public static final int Sunday = 6;

    Class(@NonNull @Days Integer day, @NonNull Integer hour){
        this.day = day;
        this.hour = hour;
    }

    Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}


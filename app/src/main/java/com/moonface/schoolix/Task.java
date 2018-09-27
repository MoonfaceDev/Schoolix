package com.moonface.schoolix;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private String title;
    private Subject subject;
    private Date date;
    private String type;
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");

    Task(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


package com.moonface.schoolix;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Semester {
    private int number;
    private Date startingDate;
    private Date endingDate;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    Semester(int number){
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    Date getStartingDate() {
        return startingDate;
    }

    void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    Date getEndingDate() {
        return endingDate;
    }

    void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }
}


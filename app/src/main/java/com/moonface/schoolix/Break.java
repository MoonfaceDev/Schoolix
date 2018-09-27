package com.moonface.schoolix;

public class Break extends Period{
    static String TAG = "break";

    Break(int number, String tag) {
        super(number, tag);
        this.number = number;
        this.tag = tag;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(int startingHour) {
        this.startingHour = startingHour;
    }

    public int getStartingMinute() {
        return startingMinute;
    }

    public void setStartingMinute(int startingMinute) {
        this.startingMinute = startingMinute;
    }

    public int getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(int endingHour) {
        this.endingHour = endingHour;
    }

    public int getEndingMinute() {
        return endingMinute;
    }

    public void setEndingMinute(int endingMinute) {
        this.endingMinute = endingMinute;
    }

    @Override
    public String getTag(){
        return tag;
    }

}


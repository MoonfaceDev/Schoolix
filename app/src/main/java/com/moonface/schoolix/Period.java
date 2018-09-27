package com.moonface.schoolix;

public class Period {
    public int number;
    int startingHour;
    int startingMinute;
    int endingHour;
    int endingMinute;
    String tag;
    public static SimpleHourFormat simpleHourFormat = new SimpleHourFormat();
    static String TAG = "period";

    Period(int number, String tag){
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

    protected static class SimpleHourFormat{
        public String format(int hours, int minutes){
            String hour_str = String.valueOf(hours);
            String minute_str = minutes > 9 ? String.valueOf(minutes) : "0"+String.valueOf(minutes);
            return hour_str + ":" + minute_str;
        }
    }

    public String getTag(){
        return tag;
    }

}


package com.moonface.schoolix;

public class Subject {
    private String title;
    private String teacher;
    private String room;
    private int drawableIndex;
    public static final int[] ids = new int[]{R.drawable.subject_icon_blue, R.drawable.subject_icon_brown, R.drawable.subject_icon_gray, R.drawable.subject_icon_green, R.drawable.subject_icon_lightblue, R.drawable.subject_icon_lightgreen, R.drawable.subject_icon_orange, R.drawable.subject_icon_pink, R.drawable.subject_icon_purple, R.drawable.subject_icon_red, R.drawable.subject_icon_yellow, R.drawable.subject_icon_cyan};

    Subject(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getRoom() {
        return room;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getDrawableIndex() {
        return drawableIndex;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDrawableIndex(int drawableIndex) {
        this.drawableIndex = drawableIndex;
    }
}
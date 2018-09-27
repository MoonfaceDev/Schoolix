package com.moonface.schoolix;

import android.graphics.drawable.Drawable;

public class Section {
    private String name;
    private Drawable icon;
    private Drawable bg;

    Section(){}

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getBg() {
        return bg;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBg(Drawable bg) {
        this.bg = bg;
    }
}

package com.sevendesign.planitprom.models;

/**
 * Created by mib on 20.08.13.
 */
public class TimelinePoint extends TimelineItem {
    private String name;
    private boolean isChecked;

    public TimelinePoint() {
        name = "";
        isChecked = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}

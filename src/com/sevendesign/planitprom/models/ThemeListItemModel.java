package com.sevendesign.planitprom.models;

/**
 * Created by mib on 08.08.13.
 */
public class ThemeListItemModel {

    private ThemeItem leftItem;
    private ThemeItem centerItem;
    private ThemeItem rightItem;

    public ThemeListItemModel() {
        leftItem = null;
        centerItem = null;
        rightItem = null;
    }

    public ThemeItem getLeftItem() {
        return leftItem;
    }

    public void setLeftItem(ThemeItem item) {
        this.leftItem = item;
    }

    public ThemeItem getCenterItem() {
        return centerItem;
    }

    public void setCenterItem(ThemeItem item) {
        this.centerItem = item;
    }

    public ThemeItem getRightItem() {
        return rightItem;
    }

    public void setRightItem(ThemeItem item) {
        this.rightItem = item;
    }
}

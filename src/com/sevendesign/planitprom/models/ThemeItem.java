package com.sevendesign.planitprom.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ThemeItem implements Parcelable {
    private String themeName;
    private int previewId;
    private int themeId;

    public ThemeItem(String themeName, int previewId, int themeId) {
        this.themeName = themeName;
        this.previewId = previewId;
        this.themeId = themeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThemeItem> CREATOR = new Creator<ThemeItem>() {
        public ThemeItem createFromParcel(Parcel parcel) {
            String themeName = parcel.readString();
            int preView = parcel.readInt();
            int theme = parcel.readInt();
            ThemeItem themeItem = new ThemeItem(themeName, preView, theme);

            return themeItem;
        }

        public ThemeItem[] newArray(int i) {
            return new ThemeItem[i];
        }
    };

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getThemeName());
        parcel.writeInt(getPreviewId());
        parcel.writeInt(getThemeId());
    }

    public int getPreviewId() {
        return previewId;
    }

    public void setPreviewId(int previewId) {
        this.previewId = previewId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}

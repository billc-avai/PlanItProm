package com.sevendesign.planitprom.ui.actions;

public enum ThemeChangedAction {
    SET_THEME;
    private String themeName;
    private ThemeChangedAction() {    }

    public String getName() {
        return themeName;
    }
    public void setName(String name) {
        this.themeName = name;
    }
}

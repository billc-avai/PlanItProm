package com.sevendesign.planitprom.ui.actions;

public enum DateSetAction {
    EVENT_DATE(),
    RETURN_DATE();

    private boolean addToBackStack;
    private Object data;

    private DateSetAction() {

    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}

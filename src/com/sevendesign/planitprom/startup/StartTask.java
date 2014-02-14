package com.sevendesign.planitprom.startup;

import android.content.Context;

public class StartTask extends BaseStartupTask {

    public StartTask(Context context) {
        super(context);
    }

    @Override
    public void process() {

    }

    @Override
    public boolean finished() {
        return true;
    }
}
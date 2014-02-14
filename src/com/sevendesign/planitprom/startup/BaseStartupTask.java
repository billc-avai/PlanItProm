package com.sevendesign.planitprom.startup;

import android.content.Context;

public abstract class BaseStartupTask {
    private Context mContext;

    public BaseStartupTask(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /* Abstract */
    public abstract void process();
    public abstract boolean finished();
}

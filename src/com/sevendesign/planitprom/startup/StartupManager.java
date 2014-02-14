package com.sevendesign.planitprom.startup;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

public class StartupManager {
    private ArrayList<BaseStartupTask>  mTasks      = new ArrayList<BaseStartupTask>();
    private ArrayList<BaseStartupTask>  mErrors     = new ArrayList<BaseStartupTask>();

    private Handler                     mHandler;
    private Context                     mContext;

    private OnStartupManagerFinished    mListener;

    public StartupManager(Context context) {
        mContext = context;
        mHandler = new Handler();
    }

    public void addTask(BaseStartupTask task) {
        mTasks.add(task);
    }

    public void setListener(OnStartupManagerFinished listener) {
        this.mListener = listener;
    }

    public void run() {
        mErrors.clear();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (final BaseStartupTask task : mTasks) {
                    task.process();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            task.finished();
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onFinishedStartupTasks();
                        }
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    public interface OnStartupManagerFinished {
        public void onFinishedStartupTasks();
    }
}

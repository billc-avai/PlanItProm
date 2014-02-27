package com.sevendesign.planitprom.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.startup.StartTask;
import com.sevendesign.planitprom.startup.StartupManager;
import com.sevendesign.planitprom.startup.TaskLoadFonts;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;

import de.greenrobot.event.EventBus;

public class SplashScreenFragment extends Fragment implements StartupManager.OnStartupManagerFinished{
    public static final String TAG = "SplashScreenFragment";
    private final int SPLASH_DELAY = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splashscreen, null);
        initActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        runStartupTask();
    }

    private void initActionBar() {
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar().hide();
    }


    private void runStartupTask() {
        StartupManager startupManager = new StartupManager(getActivity());
        startupManager.setListener(SplashScreenFragment.this);
        startupManager.addTask(new TaskLoadFonts(getActivity()));
        startupManager.addTask(new StartTask(getActivity()));
        startupManager.run();
    }

    @Override
    public void onFinishedStartupTasks() {
        new Handler().postDelayed(new Runnable(){
            public void run(){
                checkBudget();
            }
        }, SPLASH_DELAY);

    }

    private void checkBudget() {
		List<Budget> budgets = new DbManager().getBudgets();
		if (budgets.size() > 0) {
            EventBus.getDefault().post(ShowFragmentAction.PROM_BUDGET);
		} else {
			ShowFragmentAction action = ShowFragmentAction.SETTINGS;
			action.setData(new Boolean(false));
			EventBus.getDefault().post(action);
		}
    }
}




















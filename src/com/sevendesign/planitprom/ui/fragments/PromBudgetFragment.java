package com.sevendesign.planitprom.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.BudgetAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.ui.widgets.PageControl;
import com.sevendesign.planitprom.utils.Dialogs;

import de.greenrobot.event.EventBus;

public class PromBudgetFragment extends Fragment {
	public static final String TAG = "PromBudgetFragment";

    private Budget budget;
    private TextView policy;
    private Button budgetButton;
    private Button calculatorButton;
    private Button tipsButton;
    private Button timelineButton;
    private Button galleryButton;

    private ViewPager counterPager;
    private int counterCurrentPage = 0;
    private PageControl counterPageControl;
    private BudgetAdapter counterAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prom_budget, null);
        saveUI(view);
        initListeners();
        initActionBar(inflater);

        initBudget();

        return view;
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("");
        actionBar.show();
        View actionView = inflater.inflate(R.layout.action_bar_prom_budget, null);
        actionView.findViewById(R.id.settingsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFragmentAction action = ShowFragmentAction.SETTINGS;
                action.setAddBackStack(true);
                EventBus.getDefault().post(ShowFragmentAction.SETTINGS);
            }
        });
		((TextView) actionView.findViewById(R.id.title)).setText(getString(R.string.fragments_prom_budget_title));
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void initPager() {
        counterAdapter = new BudgetAdapter(getActivity(), budget);
        counterPager.setAdapter(counterAdapter);
        counterPager.setOnPageChangeListener((ViewPager.OnPageChangeListener) new CounterPageChangeListener());
    }

    private void saveUI(View view) {
        policy = (TextView) view.findViewById(R.id.policy);
        counterPager = (ViewPager) view.findViewById(R.id.counterPager);
        counterPageControl = (PageControl) view.findViewById(R.id.counter_page_control);
        budgetButton = (Button) view.findViewById(R.id.indo_trip_budget_budget_button);
        calculatorButton = (Button) view.findViewById(R.id.indo_trip_budget_calculator_button);
        tipsButton = (Button) view.findViewById(R.id.indo_trip_budget_tips_button);
        timelineButton = (Button) view.findViewById(R.id.indo_trip_budget_timeline_button);
        galleryButton = (Button) view.findViewById(R.id.indo_trip_budget_gallery_button);
    }

    private void initListeners() {
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.showPrivacyPolicy(getActivity());
            }
        });
        budgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if (budget != null) {
					EventBus.getDefault().post(ShowFragmentAction.BUDGET);
				}
            }
        });
        calculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(ShowFragmentAction.CALCULATOR);
            }
        });
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if (budget != null) {
					EventBus.getDefault().post(ShowFragmentAction.TIPS);
				}
            }
        });
        timelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if (budget != null) {
					EventBus.getDefault().post(ShowFragmentAction.TIMELINE);
				}
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if (budget != null) {
					EventBus.getDefault().post(ShowFragmentAction.GALLERY);
				}
            }
        });
    }

    private void fillCounterPages() {
		counterPageControl.setPageCount(BudgetAdapter.PAGE_COUNT);
		counterPageControl.setActiveDrawable(getResources().getDrawable(R.drawable.bullet_selected));
		counterPageControl.setInactiveDrawable(getResources().getDrawable(R.drawable.bullet_not_selected));
        counterPager.setCurrentItem(counterCurrentPage);
        changeCounterPage(counterCurrentPage);
        counterAdapter.notifyDataSetChanged();
    }

    private void changeCounterPage(int index) {
        counterPageControl.setCurrentPage(index);
    }

    private class CounterPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) { }

        @Override
        public void onPageSelected(int i) {
            counterCurrentPage = i;
            changeCounterPage(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) { }
    }

    private void initBudget() {
        List<Budget> budgets = new DbManager().getBudgets();
        if(budgets.size() > 0) {
            budget = budgets.get(0);
        } else {
            budget = null;
        }

        initPager();
        fillCounterPages();
    }
}




















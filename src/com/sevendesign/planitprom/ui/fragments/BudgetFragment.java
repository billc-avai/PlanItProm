package com.sevendesign.planitprom.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.BudgetAdapter;
import com.sevendesign.planitprom.adapters.CategoryAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.ui.widgets.PageControl;
import com.sevendesign.planitprom.utils.Dialogs;

public class BudgetFragment extends Fragment {
    public static final String TAG = "BudgetFragment";

    private ViewPager counterPager;
    int counterCurrentPage = 0;
    private PageControl counterPageControl;
    private ListView itemsList;

    private Budget budget;
    private BudgetAdapter budgetAdapter;
    private CategoryAdapter categoryAdapter;
    private final DbManager dbManager = new DbManager();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        initViews(view);
        initActionBar(inflater);

        initBudget();
        initCategories();

        return view;
    }

    private void initViews(View root) {
        counterPager = (ViewPager) root.findViewById(R.id.budget_counter_pager);
        counterPageControl = (PageControl) root.findViewById(R.id.budget_counter_page_control);
        itemsList = (ListView) root.findViewById(R.id.budget_items_list);
    }

    private void initPager() {
        budgetAdapter = new BudgetAdapter(getActivity(), budget);
        counterPager.setAdapter(budgetAdapter);
        counterPager.setOnPageChangeListener(new CounterPageChangeListener());
    }

    private void fillCounterPages() {
		counterPageControl.setPageCount(2);
		counterPageControl.setActiveDrawable(getResources().getDrawable(R.drawable.bullet_selected));
		counterPageControl.setInactiveDrawable(getResources().getDrawable(R.drawable.bullet_not_selected));
        counterPager.setCurrentItem(counterCurrentPage);
        changeCounterPage(counterCurrentPage);
        budgetAdapter.notifyDataSetChanged();
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

    private void fillCategoriesList(List<Category> categories) {
        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        itemsList.setAdapter(categoryAdapter);
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        View actionView = inflater.inflate(R.layout.action_bar_budget, null);
        actionView.findViewById(R.id.addCategoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(budget != null) {
                    Dialogs.showCreateCategory(getActivity(), budget.getId());
                }
            }
        });
        actionView.findViewById(R.id.faqButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.showInstruction(getActivity(), getString(R.string.alert_dialog_message_budget_instructions));
            }
        });
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void initBudget() {
        if(dbManager.getBudgets().size() > 0) {
            budget = dbManager.getBudgets().get(0);
            initPager();
            fillCounterPages();
        }
    }

    private void initCategories() {
        if(budget != null) {
            List<Category> categoryList = dbManager.getCategories(budget.getId());
            fillCategoriesList(categoryList);
        }
    }
}




















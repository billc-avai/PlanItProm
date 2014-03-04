package com.sevendesign.planitprom.adapters;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.ui.widgets.HorizontalProgressBar;
import com.sevendesign.planitprom.utils.BudgetUtils;

/**
 * Created by mib on 09.08.13.
 */
public class BudgetAdapter extends PagerAdapter {
	public static final int PAGE_COUNT = 3;
    private LayoutInflater inflater;
    private Budget budget;
    private String currency;
    private BigDecimal actual;
    private BigDecimal budgeted;
	private BigDecimal budgetedToDate;
    private int daysBefore;

    private Drawable progressBarRed;
    private Drawable progressBarRegular;

    public BudgetAdapter(Activity activity, Budget budget){
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(budget != null) {
            this.budget = budget;
        } else {
			this.budget = new Budget("", BigDecimal.ZERO, BigDecimal.ZERO, "0", "");
        }
        progressBarRed = activity.getResources().getDrawable(R.drawable.progress_bar_horizontal_red);
        progressBarRegular = activity.getResources().getDrawable(R.drawable.progress_bar_horizontal);
        currency = activity.getResources().getString(R.string.prom_budget_currency);
        calcData();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = null;
        if (position == 0) {
            int layoutId = R.layout.counter_item_total_budget;
            itemView = inflater.inflate(layoutId, container, false);

            TextView totalSpentText = (TextView) itemView.findViewById(R.id.counter_item_total_spent_value_text);
            TextView budgetedText = (TextView) itemView.findViewById(R.id.counter_item_total_budget_budgeted_value_text);
            final HorizontalProgressBar spentProgress = (HorizontalProgressBar) itemView.findViewById(R.id.counter_item_total_budget_spent_progress);

            StringBuilder actualSB = new StringBuilder(currency);
            actualSB.append(BudgetUtils.getMoneyValueString(actual));
            totalSpentText.setText(actualSB.toString());

            StringBuilder budgetedSB = new StringBuilder(currency);
            budgetedSB.append(BudgetUtils.getMoneyValueString(budgeted));
            budgetedText.setText(budgetedSB.toString());

            if (actual.compareTo(budgeted) == 1) {
                spentProgress.setProgressDrawable(progressBarRed);
            } else {
                spentProgress.setProgressDrawable(progressBarRegular);
            }
            spentProgress.setMax(100);
            spentProgress.post(new Runnable() {
                @Override
                public void run() {
                    double progress = 0D;
                    if (budgeted.compareTo(BigDecimal.ZERO) > 0) {
                        progress = actual.doubleValue() / budgeted.doubleValue() * 100d;
                    }
                    spentProgress.setProgress((int)progress);
                }
            });
		} else if (position == 1) {
            int layoutId = R.layout.counter_item_days_remain;
            itemView = inflater.inflate(layoutId, container, false);
            TextView daysRemainText = (TextView) itemView.findViewById(R.id.counter_item_days_remain_value_text);
            daysRemainText.setText(String.valueOf(daysBefore));
		} else if (position == 2) {
			int layoutId = R.layout.counter_item_budgeted_to_date;
			itemView = inflater.inflate(layoutId, container, false);
			TextView budgetedText = (TextView) itemView.findViewById(R.id.counter_item_budgeted_value_text);
			
			StringBuilder budgetedToDateSb = new StringBuilder(currency);
			budgetedToDateSb.append(BudgetUtils.getMoneyValueString(budgetedToDate));
			budgetedText.setText(budgetedToDateSb);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    private void calcData() {
        actual = budget.getActualBudget();
        budgeted = budget.getPlannedBudget();
		budgetedToDate = BigDecimal.valueOf(BudgetUtils.getBudgetedToDate(budget));
        daysBefore = BudgetUtils.getDaysRemain(budget);
    }
}
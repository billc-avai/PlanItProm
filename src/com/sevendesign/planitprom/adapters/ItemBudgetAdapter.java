package com.sevendesign.planitprom.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.utils.BudgetUtils;
import com.sevendesign.planitprom.utils.Utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mib on 19.08.13.
 */
public class ItemBudgetAdapter extends PagerAdapter {
    private final int PAGE_COUNT = 2;

    private LayoutInflater inflater;
    private String currency;
    private String remainString;
    private String spentString;
    private int currencyMaxLength;

    public ItemBudgetAdapter(Activity activity, Budget budget){
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resources res = activity.getResources();
        currency = res.getString(R.string.prom_budget_currency);
        currencyMaxLength = res.getInteger(R.integer.currency_max_length);
        calcData(budget);

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
        itemView = inflater.inflate(R.layout.item_budget_counter_item, container, false);
        TextView titleText = (TextView) itemView.findViewById(R.id.item_budget_counter_item_title_text);
        TextView valueText = (TextView) itemView.findViewById(R.id.item_budget_counter_item_value_text);
        valueText.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));

        if (position == 0) {
            // budget remaining
            titleText.setText(R.string.item_budget_budget_remaining_title);
            valueText.setText(remainString);
        } else if (position == 1) {
            // actual spent
            titleText.setText(R.string.item_budget_money_spent_title);
            valueText.setText(spentString);
        }

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    private void calcData(Budget budget) {
        BigDecimal actual = budget.getActualBudget();
        BigDecimal budgeted = budget.getPlannedBudget();

        DbManager dbManager = new DbManager();
        List<Category> categories = dbManager.getCategories(budget.getId());
        BigDecimal assignedBudget = new BigDecimal(0d);
        for (Category category: categories) {
            assignedBudget = assignedBudget.add(category.getPlannedCategory());
        }

        BigDecimal budgetRemaining = budgeted.subtract(assignedBudget);

        StringBuilder remainSB = new StringBuilder(currency);
        StringBuilder spentSB = new StringBuilder(currency);
        remainString = remainSB.append(BudgetUtils.getMoneyValueString(budgetRemaining)).toString();
        spentString = spentSB.append(BudgetUtils.getMoneyValueString(actual)).toString();
    }
}
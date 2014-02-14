package com.sevendesign.planitprom.adapters;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.utils.BudgetUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by mib on 16.08.13.
 */
public class CategoryAdapter extends BaseAdapter {

    private List<Category> galleryList;
    private Activity activity;
    private String zeroString;

    //private int exceedColor;
    //private int regularColor;

    private int bgColorPressed = 0;
    private int bgColorReleased = 0;
    private int textColorPressed = 0;
    private int textColorReleased = 0;

    public CategoryAdapter(Activity activity, List<Category> categoryList) {
        super();
        this.activity = activity;
        this.galleryList = categoryList;
        zeroString = activity.getString(R.string.category_zero_value);
        //exceedColor = activity.getResources().getColor(R.color.edit_text_budget_exceed);
        //regularColor = activity.getResources().getColor(R.color.budget_list_item_value);
        Resources res = activity.getResources();
		bgColorPressed = res.getColor(R.color.background_light2);
		bgColorReleased = res.getColor(R.color.background_light1);
		textColorPressed = res.getColor(R.color.text_highlighted);
		textColorReleased = res.getColor(R.color.text_medium);
    }

    @Override
    public int getCount() {
        return galleryList.size();
    }

    @Override
    public Category getItem(int position) {
        return galleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(activity);
            rootView = li.inflate(R.layout.budget_item, null);
        }

        final TextView titleText = (TextView) rootView.findViewById(R.id.budget_list_item_title_text);
        final TextView budgetedText = (TextView) rootView.findViewById(R.id.budget_list_item_budgeted_text);
        final TextView actualText = (TextView) rootView.findViewById(R.id.budget_list_item_actual_text);

        final Category category = getItem(position);
        titleText.setText(category.getTitle());

        BigDecimal planned = category.getPlannedCategory();
        BigDecimal actual = category.getActualCategory();
        String plannedString;
        String actualString;
        //Utils.updateTextColorFast(actualString, plannedString, actualText, exceedColor, regularColor);
        if (planned.compareTo(BigDecimal.ZERO) == 0) {
            plannedString = zeroString;
        } else {
            plannedString = BudgetUtils.getMoneyValueString(planned);
        }
        if (actual.compareTo(BigDecimal.ZERO) == 0) {
            actualString = zeroString;
        } else {
            actualString = BudgetUtils.getMoneyValueString(actual);
        }
        budgetedText.setText(plannedString);
        actualText.setText(actualString);

        final View rv = rootView;
        rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if(ev.getAction() == MotionEvent.ACTION_DOWN){
					budgetedText.setTextColor(textColorPressed);
					actualText.setTextColor(textColorPressed);
					rv.setBackgroundColor(bgColorPressed);
					titleText.setTextColor(textColorPressed);
//					titleText.setBackgroundResource(R.drawable.arrow_button_pressed);					
                } else if(ev.getAction() == MotionEvent.ACTION_UP){
					budgetedText.setTextColor(textColorReleased);
					actualText.setTextColor(textColorReleased);
					titleText.setTextColor(textColorReleased);
					rv.setBackgroundColor(bgColorReleased);
//					titleText.setBackgroundResource(R.drawable.arrow_button);

                    performItemClick(category);
                } else if(ev.getAction() == MotionEvent.ACTION_CANCEL){
					budgetedText.setTextColor(textColorReleased);
					actualText.setTextColor(textColorReleased);
//					titleText.setBackgroundResource(R.drawable.arrow_button);
					titleText.setTextColor(textColorReleased);
					rv.setBackgroundColor(bgColorReleased);
                }

                return true;
            }
        });

        return rootView;
    }

    private void performItemClick(Category category) {
        ShowFragmentAction action = ShowFragmentAction.ITEM_BUDGET;
        action.setAddBackStack(true);
        action.setData(category.getId());
        EventBus.getDefault().post(action);
    }
}

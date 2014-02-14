package com.sevendesign.planitprom.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;

public class SpinnerNavigator {
    public enum FragmentDescription {
        BUDGET(R.string.fragments_budget_title),
        CALCULATOR(R.string.fragments_calculator_title),
        TIME_LINE(R.string.fragments_timeline_title),
        TIPS(R.string.fragments_tips_title),
        PHOTO_GALLERY(R.string.fragments_photo_gallery_title);

        private FragmentDescription(int titleRes) {
            this.titleRes = titleRes;
        }
        private final int titleRes;
        private String getTitle(Context context) {
            if(context != null) {
                return context.getString(titleRes);
            } else {
                return "";
            }
        }
    };

    public static void initActionBar(FragmentDescription position, Activity activity) {
        ActionBar actionBar = ((SherlockFragmentActivity) activity).getSupportActionBar();
        actionBar.setCustomView(null);
        actionBar.setTitle("");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        List<String> months = new ArrayList<String>();
        for(FragmentDescription temp : FragmentDescription.values()) {
            months.add(temp.getTitle(activity));
        }

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, months);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, R.layout.action_bar_nav_spinner, R.id.spinner_textview, months);

//        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        actionBar.setListNavigationCallbacks(dataAdapter, (ActionBar.OnNavigationListener)activity);
        actionBar.setSelectedNavigationItem(position.ordinal());
    }
}

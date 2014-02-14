package com.sevendesign.planitprom.ui.actions;

import com.sevendesign.planitprom.ui.fragments.BudgetFragment;
import com.sevendesign.planitprom.ui.fragments.CalculatorFragment;
import com.sevendesign.planitprom.ui.fragments.CategoryAddFragment;
import com.sevendesign.planitprom.ui.fragments.DatePickerFragment;
import com.sevendesign.planitprom.ui.fragments.GalleryDetailsFragment;
import com.sevendesign.planitprom.ui.fragments.GalleryFragment;
import com.sevendesign.planitprom.ui.fragments.PromBudgetFragment;
import com.sevendesign.planitprom.ui.fragments.ItemBudgetFragment;
import com.sevendesign.planitprom.ui.fragments.SettingsFragment;
import com.sevendesign.planitprom.ui.fragments.SplashScreenFragment;
import com.sevendesign.planitprom.ui.fragments.ThemeSetupFragment;
import com.sevendesign.planitprom.ui.fragments.TimelineFragment;
import com.sevendesign.planitprom.ui.fragments.TipDetailsFragment;
import com.sevendesign.planitprom.ui.fragments.TipsFragment;

public enum ShowFragmentAction {
    SPLASH_SCREEN(SplashScreenFragment.TAG, false),
    THEME_SETUP(ThemeSetupFragment.TAG, false),
    SETTINGS(SettingsFragment.TAG, false),
    PROM_BUDGET(PromBudgetFragment.TAG, false),
    BUDGET(BudgetFragment.TAG, false),
    ITEM_BUDGET(ItemBudgetFragment.TAG, false),
    CALCULATOR(CalculatorFragment.TAG, false),
    CREDIT(CalculatorFragment.TAG, false),
    TIPS(TipsFragment.TAG, false),
    TIP_DETAILS(TipDetailsFragment.TAG, false),
    TIMELINE(TimelineFragment.TAG, false),
    GALLERY(GalleryFragment.TAG, false),
    GALLERY_DETAILS(GalleryDetailsFragment.TAG, false),
    CATEGORY_ADD(CategoryAddFragment.TAG, false),
    EVENT_DATE_PICKER(DatePickerFragment.TAG, false),
    RETURN_DATE_PICKER(DatePickerFragment.TAG, false);

    private final String fragmentTag;
    private boolean addToBackStack;
    private Object data;

    private ShowFragmentAction(String fragmentTag, boolean addToBackStack) {
        this.fragmentTag = fragmentTag;
        this.addToBackStack = addToBackStack;
    }

    public void setAddBackStack(boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
    }
    public boolean getAddBackStack() {
        return addToBackStack;
    }
    public String getFragmentTag() {
        return fragmentTag;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }


}

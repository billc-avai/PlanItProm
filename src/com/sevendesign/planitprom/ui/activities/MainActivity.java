package com.sevendesign.planitprom.ui.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.models.ThemeItem;
import com.sevendesign.planitprom.models.TipCategory;
import com.sevendesign.planitprom.ui.actions.DateSetAction;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.ui.actions.ThemeChangedAction;
import com.sevendesign.planitprom.ui.fragments.BackButtonListener;
import com.sevendesign.planitprom.ui.fragments.BudgetFragment;
import com.sevendesign.planitprom.ui.fragments.CalculatorFragment;
import com.sevendesign.planitprom.ui.fragments.CategoryAddFragment;
import com.sevendesign.planitprom.ui.fragments.CreditFragment;
import com.sevendesign.planitprom.ui.fragments.DatePickerFragment;
import com.sevendesign.planitprom.ui.fragments.GalleryByCategoryFragment;
import com.sevendesign.planitprom.ui.fragments.ItemBudgetFragment;
import com.sevendesign.planitprom.ui.fragments.PhotoFragment;
import com.sevendesign.planitprom.ui.fragments.PromBudgetFragment;
import com.sevendesign.planitprom.ui.fragments.SettingsFragment;
import com.sevendesign.planitprom.ui.fragments.SplashScreenFragment;
import com.sevendesign.planitprom.ui.fragments.ThemeSetupFragment;
import com.sevendesign.planitprom.ui.fragments.TimelineFragment;
import com.sevendesign.planitprom.ui.fragments.TipDetailsFragment;
import com.sevendesign.planitprom.ui.fragments.TipsFragment;
import com.sevendesign.planitprom.utils.Dialogs;
import com.sevendesign.planitprom.utils.KWFileUtils;
import com.sevendesign.planitprom.utils.PhotoUtils;
import com.sevendesign.planitprom.utils.Utils;

import de.greenrobot.event.EventBus;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener, FacebookPublisher {
    private String PREF_FILE_NAME = "THEME_STORAGE_NAME";

    private Fragment activeFragment;
    @Override
    public void onBackPressed() {
        try {
            ((BackButtonListener)activeFragment).backButtonPressed();
        } catch (Exception exc) {}
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ShowFragmentAction action = ShowFragmentAction.SPLASH_SCREEN;
        action.setAddBackStack(false);
        showFragment(new SplashScreenFragment(), action);
		
		setContentView(R.layout.activity_main);
//        useTheme();
		initActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this, ShowFragmentAction.class);
        EventBus.getDefault().register(this, ThemeChangedAction.class);
    }

    @Override
      protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("unused")
    public void onEvent(ThemeChangedAction action) {
        switch (action) {
            case SET_THEME:
                initTheme(action.getName());
                break;
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ShowFragmentAction action) {
        switch (action) {
            case SPLASH_SCREEN:
                showFragment(new SplashScreenFragment(), action);
                break;
            case THEME_SETUP:
                Boolean fromSettings = false;
                try {
                    fromSettings = (Boolean)action.getData();
                } catch(Exception exc) { }
                showFragment(new ThemeSetupFragment(fromSettings), action);
                break;
            case SETTINGS:
                showFragment(new SettingsFragment(), action);
                break;
            case PROM_BUDGET:
                showFragment(new PromBudgetFragment(), action);
                break;
            case ITEM_BUDGET:
                Object budgetData = action.getData();
                Integer id = new Integer(-1);
                if (budgetData instanceof Integer) {
                    id = (Integer) budgetData;
                }
                showFragment(ItemBudgetFragment.newInstance(id), action);
                break;
            case BUDGET:
                SpinnerNavigator.initActionBar(SpinnerNavigator.FragmentDescription.BUDGET, this);
                break;
            case CALCULATOR:
                SpinnerNavigator.initActionBar(SpinnerNavigator.FragmentDescription.CALCULATOR, this);
                break;
            case CREDIT:
                showFragment(new CreditFragment(), action);
                break;
            case TIPS:
                SpinnerNavigator.initActionBar(SpinnerNavigator.FragmentDescription.TIPS, this);
                break;
            case TIP_DETAILS:
                Object tipData = action.getData();
			TipCategory tip = new TipCategory("");
                if (tipData instanceof TipCategory) {
                    tip = (TipCategory) tipData;
                }
                showFragment(TipDetailsFragment.newInstance(tip), action);
                break;
            case TIMELINE:
                SpinnerNavigator.initActionBar(SpinnerNavigator.FragmentDescription.TIME_LINE, this);
                break;
            case GALLERY:
                SpinnerNavigator.initActionBar(SpinnerNavigator.FragmentDescription.PHOTO_GALLERY, this);
                break;
//            case GALLERY_DETAILS:
//                Object galleryDetailsData = action.getData();
//                PhotoItem photoItem = null;
//                if (galleryDetailsData instanceof PhotoItem) {
//                    photoItem = (PhotoItem) galleryDetailsData;
//                }
//                showFragment(GalleryDetailsFragment.newInstance(photoItem), action);
//                break;
		case PHOTO:
			Object photoData = action.getData();
			PhotoItem photoItem = null;
			if (photoData instanceof PhotoItem) {
				photoItem = (PhotoItem) photoData;
			}
			showFragment(PhotoFragment.newInstance(photoItem), action);
			break;
            case CATEGORY_ADD:
                Object categoryAddData = action.getData();
                long itemId = 0;
                if (categoryAddData instanceof Long) {
                    itemId = (Long) categoryAddData;
                }
                showFragment(CategoryAddFragment.newInstance(itemId), action);
                break;
            case EVENT_DATE_PICKER:{
                Calendar date = (Calendar)action.getData();
                DialogFragment newFragment = new DatePickerFragment(date, DateSetAction.EVENT_DATE);
                newFragment.show(getSupportFragmentManager(), DatePickerFragment.TAG);
            }
            break;

            case RETURN_DATE_PICKER:
                Calendar date = (Calendar)action.getData();
                DialogFragment newFragment = new DatePickerFragment(date, DateSetAction.RETURN_DATE);
                newFragment.show(getSupportFragmentManager(), DatePickerFragment.TAG);
                break;
            default:
                break;
        }
    }

    private void showFragment(Fragment fragment, ShowFragmentAction addToBackStack) {
        activeFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(addToBackStack.getAddBackStack()) {
            ft.replace(R.id.content_container, fragment, addToBackStack.getFragmentTag());
            ft.addToBackStack(fragment.getClass().getName());
        } else {
            ft.replace(R.id.content_container, fragment, addToBackStack.getFragmentTag());
            ft.disallowAddToBackStack();
        }
        ft.commit();
    }

    private void initActionBar() {
		Drawable back = getResources().getDrawable(R.drawable.actionbar_background);
//        getSupportActionBar().setLogo(R.drawable.logo_blue);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        switch (itemPosition) {
            case 0:{
                ShowFragmentAction action = ShowFragmentAction.BUDGET;
                action.setAddBackStack(true);
                showFragment(new BudgetFragment(), action);
            }
            break;

            case 1:{
                ShowFragmentAction action = ShowFragmentAction.CALCULATOR;
                action.setAddBackStack(true);
                showFragment(new CalculatorFragment(), action);
            }
            break;

            case 2:{
                ShowFragmentAction action = ShowFragmentAction.TIMELINE;
                action.setAddBackStack(true);
                showFragment(new TimelineFragment(), action);
            }
            break;

            case 3:{
                ShowFragmentAction action = ShowFragmentAction.TIPS;
                action.setAddBackStack(true);
                showFragment(new TipsFragment(), action);
            }
            break;

            case 4:{
                ShowFragmentAction action = ShowFragmentAction.GALLERY;
                action.setAddBackStack(true);
			showFragment(new GalleryByCategoryFragment(), action);
            }
            break;
        }
        return true;
    }

    private void initTheme(String themeName) {
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        preferences.edit().putString("theme", themeName).commit();
        useTheme();
    }

    private void useTheme() {
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        String themeName = preferences.getString("theme", "");

        ThemeItem theme = ThemeSetupFragment.getTheme(themeName);
        findViewById(R.id.content_container).setBackgroundResource(theme.getThemeId());
    }

    /* Facebook publishing workflow */
    @Override
    public void postPhoto(String path) {
        /* check, if photo in private storage and public storage is available, then copy photo to public storage for sharing */
        if (path.contains(getPackageName())) {
            if (KWFileUtils.isExternalStorageWritable()) {
                String externalPath = KWFileUtils.getDestinationPath(MainActivity.this, PhotoUtils.getPhotoName(), PhotoUtils.JPEG_FILE_SUFFIX);
                FileChannel inChannel = null;
                FileChannel outChannel = null;
                try {
                    inChannel = new FileInputStream(new File(path)).getChannel();
                    outChannel = new FileOutputStream(new File(externalPath)).getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    path = externalPath;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inChannel != null) {
                            inChannel.close();
                        }
                        if (outChannel != null) {
                            outChannel.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /* attempt to share photo via intent */
                Utils.shareFacebookWithIntent(MainActivity.this, path);
            } else {
                /* inform user */
                Dialogs.showInfo(MainActivity.this, getString(R.string.alert_dialog_info_message_unable_to_share));
            }
        } else {
            /* attempt to share photo via intent */
            Utils.shareFacebookWithIntent(MainActivity.this, path);
        }
    }
}

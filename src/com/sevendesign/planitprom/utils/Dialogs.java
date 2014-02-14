package com.sevendesign.planitprom.utils;

import java.math.BigDecimal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.TimelineAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.ui.actions.BudgetResetAction;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.ui.fragments.SettingsFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by mib on 12.08.13.
 */
public class Dialogs {
    public static void showPrivacyPolicy(Context context) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        dialog.setContentView(R.layout.dialog_privacy_policy);
        final WebView bodyText = (WebView) dialog.findViewById(R.id.bodyWebView);
        bodyText.loadUrl("file:///android_asset/html/privacy_policy.html");

        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showInstruction(Context context, String first) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_instruction);
        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.first)).setText(first);

        dialog.show();
    }

    public static void showCreateCategory(Context context, final int budgetId) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_create_category);
        final Button doneButton = (Button) dialog.findViewById(R.id.done);
        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.close);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String name = ((EditText) dialog.findViewById(R.id.name)).getText().toString();

				if (name.length() == 0) {
					return;
				}

                Category category = new Category(name, BigDecimal.ZERO, BigDecimal.ZERO, Utils.getCategoryBackgroundDefault(), budgetId);
                DbManager manager = new DbManager();
                long id = manager.addCategory(category);

                ShowFragmentAction action = ShowFragmentAction.CATEGORY_ADD;
                action.setData(id);
                action.setAddBackStack(true);
                EventBus.getDefault().post(action);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showCredit(Context context, String threeMonthPrice, String sixMonthPrice, String twelveMonthPrice) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_credit_info);

		((TextView) dialog.findViewById(R.id.value3)).setText(threeMonthPrice);
		((TextView) dialog.findViewById(R.id.value6)).setText(sixMonthPrice);
		((TextView) dialog.findViewById(R.id.value12)).setText(twelveMonthPrice);

        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showResetConfirm(final Context context, final int budgetId) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_reset_confirmation);
        final Button resetButton = (Button) dialog.findViewById(R.id.reset);
        final Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.close);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budgetId == SettingsFragment.NO_ID) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    DbManager manager = new DbManager();
                    manager.deleteBudget(budgetId);
                    KWFileUtils.clearDestinationPath(context, context.getResources().getString(R.string.app_photos_directory));
                    SharedPreferences preferences = context.getSharedPreferences(TimelineAdapter.DATA_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                }
                EventBus.getDefault().postSticky(BudgetResetAction.RESET_BUDGET);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showInfo(Context context, String first) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_info);
        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.first)).setText(first);

        dialog.show();
    }

}

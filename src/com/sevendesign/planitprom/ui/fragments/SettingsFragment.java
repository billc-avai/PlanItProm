package com.sevendesign.planitprom.ui.fragments;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.ui.actions.BudgetResetAction;
import com.sevendesign.planitprom.ui.actions.DateSetAction;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.ui.actions.ThemeChangedAction;
import com.sevendesign.planitprom.ui.customviews.DateField;
import com.sevendesign.planitprom.ui.widgets.KMSwitch;
import com.sevendesign.planitprom.utils.BudgetUtils;
import com.sevendesign.planitprom.utils.CalendarUtils;
import com.sevendesign.planitprom.utils.Dialogs;
import com.sevendesign.planitprom.utils.Utils;

import de.greenrobot.event.EventBus;

public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    public static final int NO_ID = -1;
    private Budget budget;
    private DbManager manager = new DbManager();
    private TextView privacyPolicy;

    private EditText budgetName;
    private EditText budgetLimit;
    private DateField eventDate;
	private String gender;

	private EditText genderEditText;
    private KMSwitch calendarSwitcher;

	private String[] eventNames;
	private String[] genders;

    private View view;
    private int currencyMaxLength;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
		eventNames = res.getStringArray(R.array.event_name_array);
        currencyMaxLength = res.getInteger(R.integer.currency_max_length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, null);
        initActionBar(inflater);
        saveUIReferences(view);
        initUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this, DateSetAction.class);
        EventBus.getDefault().register(this, BudgetResetAction.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void saveUIReferences(View view) {
        privacyPolicy = (TextView)view.findViewById(R.id.policy);
        budgetName = (EditText)view.findViewById(R.id.budgetName);
        budgetLimit = (EditText)view.findViewById(R.id.budgetLimit);
        budgetLimit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        calendarSwitcher = (KMSwitch)view.findViewById(R.id.calendarSwitcher);

        eventDate = (DateField)view.findViewById(R.id.eventDate);
		eventDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				handleDateFieldAction();
            }
        });
		
		eventDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					handleDateFieldAction();
				}
			}
		});

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.showPrivacyPolicy(getActivity());
            }
        });

        calendarSwitcher.setOn(false);

		genders = getActivity().getResources().getStringArray(R.array.settings_gender_array);
		genderEditText = (EditText) view.findViewById(R.id.gender_edit_text);
		genderEditText.setInputType(EditorInfo.TYPE_NULL);
		genderEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showGenderSelectionDialog();
			}
		});
		
		genderEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showGenderSelectionDialog();
				}
			}
		});

    }
	
	private void handleDateFieldAction() {
		ShowFragmentAction action = ShowFragmentAction.EVENT_DATE_PICKER;
		
		Calendar cal = eventDate.getCalendar();
		
		if (cal == null) {
			cal = Calendar.getInstance();
		}
		
		action.setData(cal);
		EventBus.getDefault().post(action);
		
	}
	
	private void showGenderSelectionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.settings_gender_hint)
				.setItems(genders, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						genderEditText.setText(genders[which]);
					}
				});
		builder.show();
	}

    private void initUI() {
        List<Budget> allBudgets = manager.getBudgets();

        if(allBudgets.size() > 0) {
            budget = allBudgets.get(0);
            budgetName.setText(budget.getTitle());
            if(budget.getPlannedBudget().compareTo(BigDecimal.ZERO) != 0) {
                budgetLimit.setText(BudgetUtils.getMoneyValueString(budget.getPlannedBudget()));
            } else {
                budgetLimit.setText("");
            }
			
			genderEditText.setText(budget.getGender());
            long event = Long.parseLong(budget.getEventDate());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(event);
            eventDate.setCalendar(cal);
        } else {
            budget = null;
            budgetName.setText("");
            budgetLimit.setText("");
        }
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        View actionView = inflater.inflate(R.layout.action_bar_settings, null);
        actionView.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                saveSettings();
            }
        });
        ((TextView)actionView.findViewById(R.id.title)).setText(getString(R.string.fragments_settings_title));
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
    }


    private void saveSettings() {
		String genderString = genderEditText.getText().toString();
		if (genderString == null || genderString.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Please select your gender.").setNeutralButton("OK", new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
			return;
		}

        long eventDateMillis = this.eventDate.getCalendar().getTimeInMillis();

		if (calendarSwitcher.isOn()) {
			saveNote(eventDateMillis);
		}

        String name = budgetName.getText().toString();
        BigDecimal limit = BudgetUtils.getMoneyValueFromString(budgetLimit.getText().toString());
		String eventDate = String.valueOf(eventDateMillis);
		if (gender == null) {
			gender = genderEditText.getText().toString();
		}
		
		if (budget != null && budget.getGender().equalsIgnoreCase(gender)) {
            budget.setTitle(name);
            budget.setPlannedBudget(limit);
			budget.setEventDate(eventDate);
            manager.updateBudget(budget);
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            //have not budget, must create new one
			budget = new Budget(name, limit, BigDecimal.ZERO, eventDate, gender);
            long id = manager.addBudget(budget);
			manager.addStubCategories(getActivity(), (int) id, gender);
            EventBus.getDefault().post(ShowFragmentAction.PROM_BUDGET);
        }
    }


    @SuppressWarnings("unused")
    public void onEvent(BudgetResetAction action) {
        switch (action) {
            case RESET_BUDGET:
                saveUIReferences(view);
                resetTheme();
                initUI();
                break;
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(DateSetAction action) {
        switch (action) {
            case EVENT_DATE:
			eventDate.setCalendar((Calendar) action.getData());
                break;
            default:
                break;
        }
    }

    private void resetTheme() {
        ThemeChangedAction theme = ThemeChangedAction.SET_THEME;
        theme.setName(ThemeSetupFragment.THEME_DEFAULT);
        EventBus.getDefault().post(theme);
    }


	private void saveNote(long eventDateMillis) {
		String noteTitle = eventNames[0];
		CalendarUtils.addNoteToCalendar(getActivity(), eventDateMillis, noteTitle);
	}

    private void hideKeyboard() {
        Activity activity = getActivity();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){}
    }


}
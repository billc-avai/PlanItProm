package com.sevendesign.planitprom.ui.fragments;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.sevendesign.planitprom.ui.actions.DateSetAction;

import de.greenrobot.event.EventBus;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	public static final String TAG = "datePicker";
    private Calendar date;
    private DateSetAction action;

    public DatePickerFragment(Calendar date, DateSetAction action) {
		this.date = date;
        this.action = action;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
		date.set(year, month, day);
		action.setData(date);
		EventBus.getDefault().post(action);

    }
    
}




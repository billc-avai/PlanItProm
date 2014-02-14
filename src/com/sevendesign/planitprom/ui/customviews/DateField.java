package com.sevendesign.planitprom.ui.customviews;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sevendesign.planitprom.R;

public class DateField extends RelativeLayout {

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		super.setOnFocusChangeListener(l);
		date.setOnFocusChangeListener(l);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		date.setOnClickListener(l);
	}

	private TextView date;
//    private TextView month;
//    private TextView number;
    private Calendar calendar;

    public DateField(Context context) {
        super(context);
        init();
    }

    public DateField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.custom_date_field, this);
		date = (TextView) view.findViewById(R.id.date);
//        month = (TextView)view.findViewById(R.id.month);
//        number = (TextView)view.findViewById(R.id.number);
		calendar = Calendar.getInstance();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		date.setText(String.format("%tA %<tB %<te, %<tY", calendar));

//        month.setText(String.format("%tb", calendar));
//        number.setText("" + calendar.get(Calendar.DAY_OF_MONTH));
    }

}

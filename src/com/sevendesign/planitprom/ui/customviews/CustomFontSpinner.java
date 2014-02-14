package com.sevendesign.planitprom.ui.customviews;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CustomFontSpinner extends Spinner implements CustomFontView {
	
	public CustomFontSpinner(Context context) {
		super(context);
	}

	public CustomFontSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(attrs);
	}
	
	public CustomFontSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(attrs);
	}
	
	@Override
	public void setCustomFont(AttributeSet attrs) {
		Typeface tf = CustomFontUtils.getCustomTypeface(getContext(), attrs);
		if (tf != null) {
//			setTypeface(tf);
		}
	}
	
	public class CustomFontSpinnerAdapter extends ArrayAdapter<String> {
		
		public CustomFontSpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getView(position, convertView, parent);
		}
		
	}

}

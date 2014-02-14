package com.sevendesign.planitprom.ui.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView implements CustomFontView {
	
	public CustomFontTextView(Context context) {
		super(context, null);
	}
	
	public CustomFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(attrs);
	}
	
	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(attrs);
	}
	

	@Override
	public void setCustomFont(AttributeSet attrs) {
		Typeface tf = CustomFontUtils.getCustomTypeface(getContext(), attrs);
		if (tf != null) {
			setTypeface(tf);
		}
	}

}

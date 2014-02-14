package com.sevendesign.planitprom.ui.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomFontButton extends Button implements CustomFontView {
	
	public CustomFontButton(Context context) {
		super(context, null);
	}
	
	public CustomFontButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(attrs);
	}
	
	public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
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

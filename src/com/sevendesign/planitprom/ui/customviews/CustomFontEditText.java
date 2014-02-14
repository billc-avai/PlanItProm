package com.sevendesign.planitprom.ui.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomFontEditText extends EditText implements CustomFontView {
	
	public CustomFontEditText(Context context) {
		super(context, null);
	}
	
	public CustomFontEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(attrs);
	}
	
	public CustomFontEditText(Context context, AttributeSet attrs, int defStyle) {
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

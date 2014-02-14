package com.sevendesign.planitprom.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.sevendesign.planitprom.R;

public class CustomFontUtils {
	public static Typeface getCustomTypeface(Context ctx, AttributeSet attrs) {
		if (attrs == null) {
			return null;
		}

		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontView);
		String customFont = a.getString(R.styleable.CustomFontView_custom_font);
		Typeface tf = null;
		
		try {
			if (customFont != null) {
				tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + customFont);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		a.recycle();
		return tf;
	}
}

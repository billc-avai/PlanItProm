package com.sevendesign.planitprom.utils;
import android.text.InputFilter;
import android.text.Spanned;
/**
 * Created by usern on 20.09.13.
 */
public class KWCurrencyInputFilter implements InputFilter {
    private final int decimalDigits;

    public KWCurrencyInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {
            if (source.equals(".") || source.equals(",")){
                return "";
            }
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }
        return null;
    }
}
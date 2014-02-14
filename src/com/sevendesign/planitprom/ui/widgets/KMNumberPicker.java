package com.sevendesign.planitprom.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sevendesign.planitprom.R;

/**
 * Created by mib on 14.08.13.
 */
public class KMNumberPicker extends LinearLayout {
    private ImageButton minusButton;
    private ImageButton plusButton;
    private EditText inputEdit;

    public KMNumberPicker(Context context) {
        super(context);
        init(context);
    }

    public KMNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public KMNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        initViews(context);
        initListeners();
    }

    private void initViews(Context context) {
        View view   = View.inflate(context, R.layout.km_number_picker, this);
        minusButton = (ImageButton) view.findViewById(R.id.km_number_picker_minus_button);
        plusButton = (ImageButton) view.findViewById(R.id.km_number_picker_plus_button);
        inputEdit = (EditText) view.findViewById(R.id.km_number_picker_value_edit);
    }

    private void initListeners() {
        minusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = value();
                if (value >=1) {
                    value--;
                } else if(value == 0) {
                    value = 99;
                }
                inputEdit.setText(String.valueOf(value));
            }
        });
        plusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = value();
                if(value >=99) {
                    value = 0;
                } else {
                    value++;
                }
                inputEdit.setText(String.valueOf(value));
            }
        });

    }

    private int value() {
        String valueString = inputEdit.getText().toString();
        int value = 0;
        if (!valueString.equals("")) {
            value = Integer.parseInt(valueString);
        }
        return value;
    }

    public int getValue() {
        return value();
    }

    public void setValue(int value) {
        inputEdit.setText(String.valueOf(value));
    }

    public void setTypeface(Typeface typeface) {
        inputEdit.setTypeface(typeface);
    }
}

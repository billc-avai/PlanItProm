package com.sevendesign.planitprom.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sevendesign.planitprom.R;

/**
 * Created by mib on 14.08.13.
 */
public class KMSwitch extends LinearLayout {
    private TextView onText;
    private TextView offText;
	private ViewGroup bodyLinearLayout;
    private boolean isOn = false;

    public KMSwitch(Context context) {
        super(context);
        init(context);
    }

    public KMSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public KMSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        initViews(context);
        initListeners();
        updateViewState();
    }

    private void initViews(Context context) {
        View view = View.inflate(context, R.layout.km_switch, this);
		bodyLinearLayout = (ViewGroup) view.findViewById(R.id.km_switch_body_linear_layout);
        onText = (TextView) view.findViewById(R.id.km_switch_on_text);
        offText = (TextView) view.findViewById(R.id.km_switch_off_text);
    }

    private void initListeners() {
        bodyLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
                updateViewState();
            }
        });
    }

    private void toggle() {
        isOn = !isOn;
    }

    private void updateViewState() {
        if (isOn) {
            onText.setVisibility(View.VISIBLE);
            offText.setVisibility(View.INVISIBLE);
        } else {
            onText.setVisibility(View.INVISIBLE);
            offText.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
        updateViewState();
    }

    public void setTypeface(Typeface typeface) {
        onText.setTypeface(typeface);
        offText.setTypeface(typeface);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.isOn = isOn();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setOn(ss.isOn);
        requestLayout();
    }


    static class SavedState extends BaseSavedState {
        boolean isOn;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            isOn = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(isOn);
        }

        @Override
        public String toString() {
            return "KMSwitch.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " isOn=" + isOn + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}

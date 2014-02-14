package com.sevendesign.planitprom.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created with IntelliJ IDEA.
 * User: mib
 * Date: 27.08.13
 */
public class HorizontalProgressBar extends ProgressBar {
    public HorizontalProgressBar(Context context) {
        super(context);
        initData(context);
    }

    private int spacing;

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    private void initData(Context context) {
        spacing = (int) context.getResources().getDimension(com.sevendesign.planitprom.R.dimen.progress_internal_horizontal_spacing);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        updateProgress();
        super.onDraw(canvas);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        invalidate();
    }

    private float getScale(int progress) {
        float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

        return scale;
    }

    private void updateProgress() {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) progressDrawable;
            final float scale = getScale(getProgress());
            Drawable progressBar = d.findDrawableByLayerId(com.sevendesign.planitprom.R.id.progress);

            final int width = d.getBounds().right - d.getBounds().left - spacing * 2;
            if (progressBar != null) {
                Rect progressBounds = progressBar.getBounds();
                progressBounds.left = spacing;
                progressBounds.right = progressBounds.left + (int) (width * scale + 0.5f);
                progressBar.setBounds(progressBounds);
            }
        }
    }
}

package com.sevendesign.planitprom.startup;

import android.content.Context;

public class TaskLoadFonts extends BaseStartupTask {
    public TaskLoadFonts(Context context) {
        super(context);
    }

    @Override
    public void process() {
        KMFontManager fontManager = KMFontManager.getInstance();
//        fontManager.addFont(AppDefinitions.BOLD,            "fonts/ProximaNova-Bold.otf",            getContext());
//        fontManager.addFont(AppDefinitions.REGULAR,         "fonts/ProximaNova-Regular.otf",         getContext());
//        fontManager.addFont(AppDefinitions.SEMI_BOLD,       "fonts/ProximaNova-Semibold.otf",        getContext());
    }

    @Override
    public boolean finished() {
        return true;
    }
}

package com.sevendesign.planitprom.startup;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class KMFontManager {
    private static KMFontManager mInstance;
    
    private HashMap<Integer, Typeface>   mTypefaces;

    private KMFontManager() {
        mTypefaces = new HashMap<Integer, Typeface>();
    }

    public void addFont(Integer fontIdentifier, String path, Context context) {
        boolean fontExists = false;

        try {
            Typeface typeface = getTypeface(fontIdentifier);
            if (typeface != null) {
                fontExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!fontExists) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);

            if (typeface != null) {
                mTypefaces.put(fontIdentifier, typeface);
            }
        }
    }
    
    public Typeface getTypeface(Integer fontIdentifier) {
        Typeface typeface = mTypefaces.get(fontIdentifier);
        return typeface;
    }

    public static KMFontManager getInstance() {
        if (mInstance == null) {
            mInstance = new KMFontManager();
        }
        return mInstance;
    }
}

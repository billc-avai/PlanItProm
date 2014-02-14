package com.sevendesign.planitprom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import com.sevendesign.planitprom.R;

import java.io.File;

/**
 * Created by mib on 30.08.13.
 */
public class KWFileUtils {
    /* creates file path for storing data in SD card or in local storage (depending on SD card availability and it's access permissions) */
    public static String getDestinationPath(Activity activity, String fileName, String fileSuffix) {
        String destinationPath = "";
        /* create necessary path */
        if (isExternalStorageWritable()) {
            /* get directory */
            String directoryName = activity.getResources().getString(R.string.app_photos_directory);
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + directoryName);
            if (!folder.isDirectory()) folder.delete(); /* delete file to get ability create folder there */
            if (!folder.exists()) folder.mkdirs();

            if (folder != null) {
                destinationPath = folder.getAbsolutePath() + "/" + fileName + fileSuffix;
            }
        } else {
            /* no external storage case. Create local file path without special directory */
            destinationPath = (new ContextWrapper(activity)).getFilesDir() + "/" + fileName + fileSuffix;
        }

        return destinationPath;
    }

    /* returns true if external storage available and writable */
    public static boolean isExternalStorageWritable() {
        /* check external storage state */
        boolean isExternalStorageAvailable = false;
        boolean isExternalStorageWritable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            isExternalStorageAvailable = isExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            isExternalStorageAvailable = true;
            isExternalStorageWritable = false;
        } else {
            isExternalStorageAvailable = isExternalStorageWritable = false;
        }
        return isExternalStorageAvailable && isExternalStorageWritable;
    }

    public static void clearDestinationPath(Context context, String directoryName) {
        String destinationPath = "";
        /* create necessary path */
        if (isExternalStorageWritable()) {
            /* get directory */
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + directoryName);
            if (!folder.isDirectory()) folder.delete(); /* delete file to get ability create folder there */
            if (!folder.exists()) folder.mkdirs();

            if (folder != null) {
                destinationPath = folder.getAbsolutePath();
            }
        } else {
            /* no external storage case. Create local file path without special directory */
            destinationPath = (new ContextWrapper(context)).getFilesDir().toString();
        }

        File dir = new File(destinationPath);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

}

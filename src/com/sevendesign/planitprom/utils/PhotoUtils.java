package com.sevendesign.planitprom.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.util.Calendar;

/**
 * Created by mib on 30.08.13.
 */
public class PhotoUtils {
    public static final int ACTION_TAKE_PHOTO = 101;
    public static final String JPEG_FILE_SUFFIX = ".jpeg";

    public static void takePhoto(Fragment callerFragment, String photoPath) {
        dispatchTakePictureIntent(callerFragment, ACTION_TAKE_PHOTO, photoPath);
    }

    private static void dispatchTakePictureIntent(Fragment callerFragment, int actionCode, String pathString) {
        File f = new File(pathString);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        callerFragment.startActivityForResult(takePictureIntent, actionCode);
    }

    public static Bitmap getPhotoBitmap(int width, int height, String pathString) {
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathString, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((width > 0) || (height > 0)) {
            scaleFactor = Math.min(photoW / width, photoH / height);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        return BitmapFactory.decodeFile(pathString, bmOptions);
    }

    public static String getPhotoName() {
        Calendar calendar = Calendar.getInstance();

        return String.valueOf(calendar.getTimeInMillis());
    }

}

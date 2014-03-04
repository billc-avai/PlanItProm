package com.sevendesign.planitprom.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.PhotoItem;

/**
 * Created by mib on 30.08.13.
 */
public class PhotoUtils {
    public static final int ACTION_TAKE_PHOTO = 101;
	public static final int ACTION_PICK_PHOTO = 102;
    public static final String JPEG_FILE_SUFFIX = ".jpeg";

    public static void takePhoto(Fragment callerFragment, String photoPath) {
		dispatchTakePictureIntent(callerFragment, photoPath);
    }

	private static void dispatchTakePictureIntent(final Fragment callerFragment, String pathString) {
		final File f = new File(pathString);
		
		AlertDialog.Builder builder = new Builder(callerFragment.getActivity());
		
		String addExistingPicture = callerFragment.getActivity().getResources().getString(R.string.picture_dialog_item_add_existing);
		String takeNewPicture = callerFragment.getActivity().getResources().getString(R.string.picture_dialog_item_take_new);
		
		String[] types = { addExistingPicture, takeNewPicture };
		builder.setItems(types, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
				switch (which) {
				case 0:
					Intent choosePhotoIntent = new Intent(Intent.ACTION_PICK);
					choosePhotoIntent.setType("image/*");
					callerFragment.startActivityForResult(choosePhotoIntent, ACTION_PICK_PHOTO);
					break;
				case 1:
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					callerFragment.startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
					break;
				}
			}
			
		});
		
		builder.show();

    }

	public static Bitmap getPhotoBitmap(Context ctx, int width, int height, String pathString) {
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
		
		try {
			if (pathString.startsWith("content")) {
				BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(Uri.parse(pathString)), null, bmOptions);
			} else {
				BitmapFactory.decodeFile(pathString, bmOptions);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

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
		
		try {
			if (pathString.startsWith("content")) {
				return BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(Uri.parse(pathString)), null, bmOptions);
			} else {
				return BitmapFactory.decodeFile(pathString, bmOptions);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
    }

    public static String getPhotoName() {
        Calendar calendar = Calendar.getInstance();

        return String.valueOf(calendar.getTimeInMillis());
    }
	
	public static void handlePhotoResult(Activity activity, int requestCode, Intent data, int categoryId, String photoPath) {
		String title = "";
		String merchant = "";
		BigDecimal cost = BigDecimal.ZERO;
		String notes = "";
		Calendar cal = Calendar.getInstance();
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(String.format("%tB", cal));
		dataBuilder.append(" ");
		dataBuilder.append(cal.get(Calendar.DAY_OF_MONTH));
		dataBuilder.append(", ");
		dataBuilder.append(cal.get(Calendar.YEAR));
		String date = dataBuilder.toString();
		
		String photoPathToUse = photoPath;
		
		if (requestCode == PhotoUtils.ACTION_PICK_PHOTO) {
			Uri imageUri = data.getData();
			photoPathToUse = imageUri.toString();
		}
		
		DbManager manager = new DbManager();
		manager.addPhotoItem(new PhotoItem(title, photoPathToUse, cost, merchant, notes, date, categoryId));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(activity.getResources().getString(R.string.alert_dialog_photo_saved_message));
		builder.setCancelable(true);
		builder.setNeutralButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		
		builder.show();
		
	}

}

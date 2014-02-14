package com.sevendesign.planitprom.utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.text.InputFilter;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.models.Category;

/**
 * Created by mib on 13.08.13.
 */
public class Utils {
    public static int getScreenWidth(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        int width;
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            disp.getSize(size);
            width = size.x;
        } else {
            width = disp.getWidth();  // deprecated
        }
        int displayWidth = width;

        return displayWidth;
    }

    public static String getStringPrice(double price) {
		return String.format("$%.2f", Utils.round(price, 2));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /* Calculate total item credit price using annuity calculations */
    public static double calculateCreditPrice(double totalCost, double yearTax, int monthsQuantity) {
        double monthTax = yearTax / 12d / 100d;
        double power = Math.pow(1d + (double) monthTax, monthsQuantity);
        double k = monthTax * power / (power - 1d); // annuity coefficient

        Double result = k * totalCost * monthsQuantity;
        if(result.isNaN()) {
            result = 0d;
        }
        return result;
    }

	public static List<Category> getStubCategories(Context context, int budgetId, String gender) {
        List<Category> categories = new ArrayList<Category>();

		String[] categoriesArray;
		
		if (gender.equalsIgnoreCase("male")) {
			categoriesArray = context.getResources().getStringArray(R.array.categories_male);
		} else {
			categoriesArray = context.getResources().getStringArray(R.array.categories_female);
		}
		
		for (int i = 0; i < categoriesArray.length; i++) {
			categories.add(new Category(categoriesArray[i], BigDecimal.ZERO, BigDecimal.ZERO, getCategoryHeaderBackground(context, i, gender), budgetId));
        }
        return categories;
    }

    public static void updateTextColor(EditText actualEdit, EditText budgetedEdit, Resources resources) {
        String actualString = actualEdit.getText().toString();
        String budgetedString = budgetedEdit.getText().toString();
        if(actualString == null || actualString.equalsIgnoreCase("")) return;
        if(budgetedString == null || budgetedString.equalsIgnoreCase("")) return;
        float actual = 0f;
        try {
            actual = Float.valueOf(actualString);
        } catch(Exception exc) { }

        float budgeted = 0f;
        try {
            budgeted = Float.valueOf(budgetedString);
        } catch(Exception exc){}
        if (actual > budgeted) {
            // red
            actualEdit.setTextColor(resources.getColor(R.color.edit_text_budget_exceed));
        } else {
            // black
            actualEdit.setTextColor(resources.getColor(R.color.edit_text_value));
        }
    }

    public static void updateTextColorFast(String actualString, String budgetedString, TextView actualEdit, int exceedColor, int regularColor) {
        float actual = 0f;
        try {
            actual = Float.valueOf(actualString);
        } catch(Exception exc) { }

        float budgeted = 0f;
        try {
            budgeted = Float.valueOf(budgetedString);
        } catch(Exception exc){}
        if (actual > budgeted) {
            // red
            actualEdit.setTextColor(exceedColor);
        } else {
            // black
            actualEdit.setTextColor(regularColor);
        }
    }

	private static int getCategoryHeaderBackground(Context context, int index, String gender) {
		String resourceName = null;
		int resourceId = 0;
		
		try {
			if (gender.equalsIgnoreCase("male")) {
				String[] header_images = context.getResources().getStringArray(R.array.category_headers_male);
				resourceName = header_images[index];
			} else {
				String[] header_images = context.getResources().getStringArray(R.array.category_headers_female);
				resourceName = header_images[index];
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		if (resourceName != null) {
			resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
		}
		
		return resourceId;
    }

    public static int getCategoryBackgroundDefault() {
		return R.drawable.bg1;
    }

    public static void shareFacebookWithIntent(Activity activity, String imagePath) {
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            File photoFile = new File(imagePath);
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
            //share.putExtra(Intent.EXTRA_TEXT, "My Image");
            activity.startActivity(Intent.createChooser(share, "Share Image"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InputFilter[] getCurrencyInputFilters(int maxLength) {
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.LengthFilter(maxLength);
        filterArray[1] = new KWCurrencyInputFilter(2);
        return filterArray;
    }
}

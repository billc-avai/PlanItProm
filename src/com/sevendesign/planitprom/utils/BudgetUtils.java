package com.sevendesign.planitprom.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;

import com.sevendesign.planitprom.database.models.Budget;

/**
 * Created by mib on 29.08.13.
 */
public class BudgetUtils {

	public static int getDaysRemain(Budget budget) {
		String departureString = budget.getEventDate();
		Calendar currentCalendar = Calendar.getInstance();
		Calendar departureCalendar = Calendar.getInstance();
		departureCalendar.setTimeInMillis(Long.valueOf(departureString));
		
		long currentMillis = currentCalendar.getTimeInMillis();
		long departureMillis = departureCalendar.getTimeInMillis();
		
		int daysRemain = 0;
		if (departureCalendar.after(currentCalendar)) {
			long diffMillis = departureMillis - currentMillis;
			daysRemain = (int) Math.ceil((double) diffMillis / 86400000d);
		}
		
		return daysRemain;
	}

//    public static BigDecimal getBudgetPerDay(Budget budget) {
//        String departureString = budget.getDepartureDate();
//        Calendar departureCalendar = Calendar.getInstance();
//        departureCalendar.setTimeInMillis(Long.valueOf(departureString));
//
//        String returnString = budget.getReturnDate();
//        Calendar returnCalendar = Calendar.getInstance();
//        returnCalendar.setTimeInMillis(Long.valueOf(returnString));
//
//        long departureMillis = departureCalendar.getTimeInMillis();
//        long returnMillis = returnCalendar.getTimeInMillis();
//
//        int daysInTrip = 0;
//        if (returnCalendar.after(departureCalendar)) {
//            long inTrip = returnMillis - departureMillis;
//            daysInTrip = (int) (inTrip / 86400000L);
//        }
//
//        BigDecimal budgetPerDay = BigDecimal.ZERO;
//        if (daysInTrip > 0) {
//            budgetPerDay = budget.getPlannedBudget().divide(new BigDecimal(daysInTrip), 2, RoundingMode.HALF_UP);
//        }
//
//        return budgetPerDay;
//    }

    public static String getMoneyValueString(float value) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(symbols);
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        String formattedValue = df.format(value);

        return formattedValue.replaceAll(",", ""); // exclude commas to get simple float appearance with two fraction characters
    }

    public static BigDecimal getMoneyValueFromString(String value) {
        BigDecimal limit = BigDecimal.ZERO;
        try {
            limit = new BigDecimal(value);
        } catch (Exception exc){}

        return limit;
    }

    public static String getMoneyValueString(BigDecimal value) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(symbols);
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        String formattedValue = df.format(value);

        return formattedValue.replaceAll(",", ""); // exclude commas to get simple float appearance with two fraction characters
    }
}

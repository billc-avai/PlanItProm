package com.sevendesign.planitprom.database.tables;

import android.provider.BaseColumns;

public final class BudgetsTableEntries implements BaseColumns {
    private BudgetsTableEntries() {	}

	public static final String TABLE_NAME = "BudgetsTable";
	public static final String TITLE = "title";
	public static final String PLANNED_BUDGET = "planned_budget";
	public static final String ACTUAL_BUDGET = "actual_budget";
//	public static final String TRAVEL_PERIOD = "travel_period";
	public static final String DEPARTURE_DATE = "departure_period";
//    public static final String RETURN_DATE = "return_date";
//	public static final String ADULTS_COUNT = "adults_count";
//    public static final String CHILDREN_COUNT = "children_count";
	public static final String GENDER = "gender";

//    public static final String[] ALL_COLUMN_NAMES = new String[] { _ID, TITLE, PLANNED_BUDGET, ACTUAL_BUDGET, TRAVEL_PERIOD, DEPARTURE_DATE, RETURN_DATE, ADULTS_COUNT, CHILDREN_COUNT};
	public static final String[] ALL_COLUMN_NAMES = new String[] { _ID, TITLE, PLANNED_BUDGET, ACTUAL_BUDGET, DEPARTURE_DATE, GENDER };
}

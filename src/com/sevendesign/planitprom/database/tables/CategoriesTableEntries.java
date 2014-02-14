package com.sevendesign.planitprom.database.tables;

import android.provider.BaseColumns;

public final class CategoriesTableEntries implements BaseColumns {
    private CategoriesTableEntries() {	}

	public static final String TABLE_NAME = "CategoriesTable";
	public static final String TITLE = "title";
	public static final String PLANNED_CATEGORY = "planned_category";
	public static final String ACTUAL_CATEGORY = "actual_category";
    public static final String BACKGROUND_ID = "background_id";
	public static final String BUDGET_ID = "budget_id";

	public static final String[] ALL_COLUMN_NAMES = new String[] { _ID, TITLE, PLANNED_CATEGORY, ACTUAL_CATEGORY, BACKGROUND_ID, BUDGET_ID };
}

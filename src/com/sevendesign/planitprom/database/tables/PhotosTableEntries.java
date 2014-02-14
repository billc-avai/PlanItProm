package com.sevendesign.planitprom.database.tables;

import android.provider.BaseColumns;

public final class PhotosTableEntries implements BaseColumns {
    private PhotosTableEntries() {	}

	public static final String TABLE_NAME = "PhotosTable";
	public static final String TITLE = "title";
	public static final String PATH = "path";
	public static final String COST = "cost";
	public static final String MERCHANT = "merchant";
    public static final String NOTES = "notes";
    public static final String DATE = "date";
    public static final String CATEGORY_ID = "category_id";

	public static final String[] ALL_COLUMN_NAMES = new String[] { _ID, TITLE, PATH, COST, MERCHANT, NOTES, DATE, CATEGORY_ID};
}

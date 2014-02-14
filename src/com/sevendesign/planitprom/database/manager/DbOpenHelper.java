package com.sevendesign.planitprom.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sevendesign.planitprom.database.tables.BudgetsTableEntries;
import com.sevendesign.planitprom.database.tables.CategoriesTableEntries;
import com.sevendesign.planitprom.database.tables.PhotosTableEntries;
import com.sevendesign.planitprom.utils.DebugLog;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 4;
	private static final String DB_NAME = "vocationTracker.db";

	private final String CREATE_BUDGETS_TABLE = "CREATE TABLE "
			+ BudgetsTableEntries.TABLE_NAME + " ("
			+ BudgetsTableEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ BudgetsTableEntries.TITLE + " TEXT NOT NULL, "
			+ BudgetsTableEntries.PLANNED_BUDGET + " TEXT NOT NULL, "
			+ BudgetsTableEntries.ACTUAL_BUDGET + " TEXT NOT NULL, "
			//			+ BudgetsTableEntries.TRAVEL_PERIOD + " TEXT NOT NULL, "
//			+ BudgetsTableEntries.DEPARTURE_DATE + " TEXT NOT NULL, "
			//            + BudgetsTableEntries.RETURN_DATE + " TEXT NOT NULL, "
//            + BudgetsTableEntries.ADULTS_COUNT + " INTEGER NOT NULL, "
//			+ BudgetsTableEntries.CHILDREN_COUNT + " INTEGER NOT NULL" 
			+ BudgetsTableEntries.DEPARTURE_DATE + " TEXT NOT NULL, "
			+ BudgetsTableEntries.GENDER + " TEXT NOT NULL " + ")";

	private final String CREATE_CATEGORIES_TABLE = "CREATE TABLE "
			+ CategoriesTableEntries.TABLE_NAME + " ("
			+ CategoriesTableEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CategoriesTableEntries.TITLE + " TEXT NOT NULL, "
			+ CategoriesTableEntries.PLANNED_CATEGORY + " TEXT NOT NULL, "
			+ CategoriesTableEntries.ACTUAL_CATEGORY + " TEXT NOT NULL, "
            + CategoriesTableEntries.BACKGROUND_ID + " INTEGER NOT NULL, "
			+ CategoriesTableEntries.BUDGET_ID + " INTEGER NOT NULL)";

	private final String CREATE_PHOTOS_TABLE = "CREATE TABLE "
			+ PhotosTableEntries.TABLE_NAME + " ("
			+ PhotosTableEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PhotosTableEntries.TITLE + " TEXT NOT NULL, "
			+ PhotosTableEntries.PATH + " TEXT NOT NULL, "
			+ PhotosTableEntries.COST + " TEXT NOT NULL, "
			+ PhotosTableEntries.MERCHANT + " TEXT NOT NULL, "
            + PhotosTableEntries.NOTES + " TEXT NOT NULL, "
            + PhotosTableEntries.DATE + " TEXT NOT NULL, "
			+ PhotosTableEntries.CATEGORY_ID + " INTEGER NOT NULL " + ")";

	public DbOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DebugLog.logMethod();
		db.execSQL(CREATE_BUDGETS_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_PHOTOS_TABLE);

		DebugLog.i("DB tables successfuly created!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BudgetsTableEntries.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CategoriesTableEntries.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PhotosTableEntries.TABLE_NAME);

		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}

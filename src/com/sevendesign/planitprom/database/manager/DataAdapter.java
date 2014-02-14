package com.sevendesign.planitprom.database.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sevendesign.planitprom.utils.DebugLog;

/** Provides access to DB. */
public class DataAdapter {

	private static DataAdapter instance = null;
	private final DbOpenHelper openHelper;

	/** Initialize DbAdapter instance. Must be called first in Main Service. */
	public static void initInstance(Context context) {
		instance = new DataAdapter(context);
	}

	/** Gets DataAdapter instance. */
	public static synchronized DataAdapter getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					"DataAdapter should be initialized first.");
		} else {
			return instance;
		}
	}

	private DataAdapter(Context context) {
		this.openHelper = new DbOpenHelper(context);
	}

	/**
	 * Query the given table, returning a Cursor over the result set.
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return {@code Cursor}
	 */
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {

		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		Cursor cursor;

		try {
			cursor = dataBase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
		} catch (Exception e) {
			DebugLog.logException(e);
			return null;
		}

		return cursor;
	}

	/**
	 * Convenience method for inserting a row into the database.
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 */
	public long insert(String table, String nullColumnHack, ContentValues values) {
		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		dataBase.beginTransaction();
		long id = 1;
		try {
			id = dataBase.insert(table, nullColumnHack, values);
			dataBase.setTransactionSuccessful();
		} catch (Exception e) {
			DebugLog.logException(e);
		} finally {
			dataBase.endTransaction();
		}
		return id;
	}

	/**
	 * Convenience method which perform update of the row if insert was unsuccessful.
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @param whereArgs
	 * @param whereClause
	 */
	public long insertOrUpdate(String table, String nullColumnHack, ContentValues values, String whereClause,
			String[] whereArgs) {

		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		dataBase.beginTransaction();
		long id = 1;
		try {
			id = dataBase.insert(table, nullColumnHack, values);
			dataBase.setTransactionSuccessful();
		} finally {
			dataBase.endTransaction();
		}

		if (id < 0) {
			update(table, values, whereClause, whereArgs);
		}
        return id;
	}

	/**
	 * Convenience method for updating rows in the database.
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public void update(String table, ContentValues values, String whereClause, String[] whereArgs) {

		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		dataBase.beginTransaction();
		try {
			dataBase.update(table, values, whereClause, whereArgs);
			dataBase.setTransactionSuccessful();
		} catch (Exception e) {
			DebugLog.logException(e);
		} finally {
			dataBase.endTransaction();
		}
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 */
	public int remove(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		dataBase.beginTransaction();
		int result = -1;
		try {
			result = dataBase.delete(table, whereClause, whereArgs);
			dataBase.setTransactionSuccessful();
		} catch (Exception e) {
			DebugLog.logException(e);
		} finally {
			dataBase.endTransaction();
		}
		return result;
	}

	/**
	 * Method for getting the count of table rows by specified criteria
	 * @param table
	 *            - table name
	 * @param selection
	 *            - string representing WHERE criteria with ? wildcards
	 * @param selectionArgs
	 *            - array of parameters which should be inserted in WHERE clause
	 * @return count of rows satisfying the specified criteria
	 */
	public int count(String table, String selection, String[] selectionArgs) {
		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		Cursor cursor = null;

		try {
			cursor = dataBase.rawQuery("SELECT COUNT(*) FROM " + table
					+ " WHERE " + selection, selectionArgs);
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch (Exception e) {
			DebugLog.logException(e);
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Runs the provided SQL and returns a Cursor over the result set.
	 * @param sql
	 * @param selectionArgs
	 * @return {@code Cursor}
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		SQLiteDatabase dataBase = openHelper.getReadableDatabase();
		Cursor cursor;

		try {
			cursor = dataBase.rawQuery(sql, selectionArgs);
		} catch (Exception e) {
			DebugLog.logException(e);
			return null;
		}

		return cursor;
	}

	/** Begins a transaction in EXCLUSIVE mode. */
	public void beginTransaction() {
		openHelper.getReadableDatabase().beginTransaction();
	}

	/** Marks the current transaction as successful. */
	public void setTransactionSuccessful() {
		openHelper.getReadableDatabase().setTransactionSuccessful();
	}

	/** End a transaction. */
	public void endTransaction() {
		openHelper.getReadableDatabase().endTransaction();
	}

	public SQLiteDatabase getWritableDb() {
		return openHelper.getWritableDatabase();
	}
}

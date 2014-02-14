package com.sevendesign.planitprom.database.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.database.tables.BudgetsTableEntries;
import com.sevendesign.planitprom.database.tables.CategoriesTableEntries;
import com.sevendesign.planitprom.database.tables.PhotosTableEntries;
import com.sevendesign.planitprom.utils.Utils;

public class DbManager {
	private final DataAdapter dataAdapter;

	public DbManager() {
		this.dataAdapter = DataAdapter.getInstance();
	}

	public long addBudget(Budget budget) {
        String whereClause = BudgetsTableEntries._ID + "=?";
		String[] whereArgs = new String[] { "0" };
		return dataAdapter.insertOrUpdate(BudgetsTableEntries.TABLE_NAME, null, budget.toContentValues(), whereClause, whereArgs);
	}

    public void deleteBudget(int budgetId) {
        String selectionCategoryId = BudgetsTableEntries._ID + "=?";
        String[] selectionCategoryArgs = new String[] { String.valueOf(budgetId) };
        dataAdapter.remove(BudgetsTableEntries.TABLE_NAME, selectionCategoryId, selectionCategoryArgs);
        deleteAllCategories(budgetId);
        deletePhotoItems();
    }

    public void updateBudget(Budget budget) {
        String whereClause = BudgetsTableEntries._ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(budget.getId()) };
        dataAdapter.update(BudgetsTableEntries.TABLE_NAME, budget.toContentValues(), whereClause, whereArgs);
    }

    public void updateCategory(Category category) {
        String whereClause = CategoriesTableEntries._ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(category.getId()) };
        dataAdapter.update(CategoriesTableEntries.TABLE_NAME, category.toContentValues(), whereClause, whereArgs);

        Budget budget = getBudget(category.getBudgetId());
        List<Category> allCategories = getCategories(category.getBudgetId());
        BigDecimal result = BigDecimal.ZERO;
        for(Category tempCategory : allCategories) {
            try {
                result = result.add(tempCategory.getActualCategory());// TODO: check try/catch reasons of block
            } catch(Exception exc){}
        }
        budget.setActualBudget(result);
        updateBudget(budget);
    }

	public List<Budget> getBudgets() {
		Cursor cursor = dataAdapter.query(BudgetsTableEntries.TABLE_NAME, BudgetsTableEntries.ALL_COLUMN_NAMES, null, null, null, null, null);

		if (cursor == null) {
			return Collections.emptyList();
		}

		if (!cursor.moveToFirst()) {
			cursor.close();
			return Collections.emptyList();
		}

		List<Budget> result = new ArrayList<Budget>();
		do {
			result.add(ObjectsConverter.convertBudgetFromCursor(cursor));
		} while (cursor.moveToNext());
		cursor.close();

		return result;
	}

    public Budget getBudget(int budgetId) {
        String selectionByBudgetId = BudgetsTableEntries._ID + "=?";
        String[] selectionByLevelArgs = new String[] { String.valueOf(budgetId) };
        Cursor cursor = dataAdapter.query(BudgetsTableEntries.TABLE_NAME,
                BudgetsTableEntries.ALL_COLUMN_NAMES, selectionByBudgetId,
                selectionByLevelArgs, null, null, null);

        if (cursor == null) {
            return null;
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return ObjectsConverter.convertBudgetFromCursor(cursor);
    }

    public List<Category> getCategories(int budgetId) {
		String selectionByBudgetId = CategoriesTableEntries.BUDGET_ID + "=?";
        String[] selectionByLevelArgs = new String[] { String.valueOf(budgetId) };
		Cursor cursor = dataAdapter.query(CategoriesTableEntries.TABLE_NAME,
                CategoriesTableEntries.ALL_COLUMN_NAMES, selectionByBudgetId,
				selectionByLevelArgs, null, null, null);

        if (cursor == null) {
            return Collections.emptyList();
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return Collections.emptyList();
        }

        List<Category> result = new ArrayList<Category>();
        do {
            result.add(ObjectsConverter.convertCategoryFromCursor(cursor));
        } while (cursor.moveToNext());
        cursor.close();

        return result;
	}

    public Category getCategory(long categoryId) {
        String selectionByBudgetId = CategoriesTableEntries._ID + "=?";
        String[] selectionByLevelArgs = new String[] { String.valueOf(categoryId) };
        Cursor cursor = dataAdapter.query(CategoriesTableEntries.TABLE_NAME,
                CategoriesTableEntries.ALL_COLUMN_NAMES, selectionByBudgetId,
                selectionByLevelArgs, null, null, null);

        if (cursor == null) {
            return null;
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return ObjectsConverter.convertCategoryFromCursor(cursor);
    }

	public void addStubCategories(Context context, int budgetId, String gender) {
		List<Category> stubCategories = Utils.getStubCategories(context, budgetId, gender);
		
		for (Category category : stubCategories) {
            dataAdapter.insert(CategoriesTableEntries.TABLE_NAME, null, category.toContentValues());
        }
    }

    public long addCategory(Category category) {
        long id = 0;
        if(category != null) {
            id = dataAdapter.insert(CategoriesTableEntries.TABLE_NAME, null, category.toContentValues());
        }
        return id;
    }

    public List<PhotoItem> getPhotoItems() {
        Cursor cursor = dataAdapter.query(PhotosTableEntries.TABLE_NAME, PhotosTableEntries.ALL_COLUMN_NAMES, null, null, null, null, null);
        if (cursor == null) {
            return Collections.emptyList();
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return Collections.emptyList();
        }

        List<PhotoItem> result = new ArrayList<PhotoItem>();
        do {
            result.add(ObjectsConverter.convertPhotoItemFromCursor(cursor));
        } while (cursor.moveToNext());
        cursor.close();

        return result;
    }

    public void addPhotoItem(PhotoItem photoItem) {
        dataAdapter.insert(PhotosTableEntries.TABLE_NAME, null, photoItem.toContentValues());
    }

    public void deletePhotoItems() {
        dataAdapter.remove(PhotosTableEntries.TABLE_NAME, null, null);
    }

    public void updatePhotoItem(PhotoItem photoItem) {
        String whereClause = PhotosTableEntries._ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(photoItem.getId()) };
        dataAdapter.update(PhotosTableEntries.TABLE_NAME, photoItem.toContentValues(), whereClause, whereArgs);
    }

    public void removePhotoItem(PhotoItem photoItem) {
        String selectionCategoryId = PhotosTableEntries._ID + "=?";
        String[] selectionCategoryArgs = new String[] { String.valueOf(photoItem.getId()) };
        dataAdapter.remove(PhotosTableEntries.TABLE_NAME, selectionCategoryId, selectionCategoryArgs);
    }

    public void deleteCategory(long categoryId) {
        String selectionCategoryId = CategoriesTableEntries._ID + "=?";
        String[] selectionCategoryArgs = new String[] { String.valueOf(categoryId) };
        dataAdapter.remove(CategoriesTableEntries.TABLE_NAME, selectionCategoryId, selectionCategoryArgs);
    }

    public void deleteAllCategories(int budgetId) {
        String selectionCategoryId = CategoriesTableEntries.BUDGET_ID + "=?";
        String[] selectionCategoryArgs = new String[] { String.valueOf(budgetId) };
        dataAdapter.remove(CategoriesTableEntries.TABLE_NAME, selectionCategoryId, selectionCategoryArgs);
    }
}
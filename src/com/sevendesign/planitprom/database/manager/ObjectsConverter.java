package com.sevendesign.planitprom.database.manager;

import java.math.BigDecimal;

import android.database.Cursor;

import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.database.tables.BudgetsTableEntries;
import com.sevendesign.planitprom.database.tables.CategoriesTableEntries;
import com.sevendesign.planitprom.database.tables.PhotosTableEntries;

final class ObjectsConverter {
    private ObjectsConverter() { }

    static PhotoItem convertPhotoItemFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(PhotosTableEntries._ID));
        String title = cursor.getString(cursor.getColumnIndex(PhotosTableEntries.TITLE));
        String path = cursor.getString(cursor.getColumnIndex(PhotosTableEntries.PATH));
        BigDecimal cost = new BigDecimal(cursor.getString(cursor.getColumnIndex(PhotosTableEntries.COST)));
        String merchant = cursor.getString(cursor.getColumnIndex(PhotosTableEntries.MERCHANT));
        String notes = cursor.getString(cursor.getColumnIndex(PhotosTableEntries.NOTES));
        String date = cursor.getString(cursor.getColumnIndex(PhotosTableEntries.DATE));
        int categoryId = cursor.getInt(cursor.getColumnIndex(PhotosTableEntries.CATEGORY_ID));
        PhotoItem photoItem = new PhotoItem(title, path, cost, merchant, notes, date, categoryId);
        photoItem.setId(id);
        return photoItem;
    }


	static Budget convertBudgetFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(BudgetsTableEntries._ID));
        String title = cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.TITLE));
        BigDecimal plannedBudget = new BigDecimal(cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.PLANNED_BUDGET)));
        BigDecimal actualBudget = new BigDecimal(cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.ACTUAL_BUDGET)));
//		String travelPeriod = cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.TRAVEL_PERIOD));
		String departureDate = cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.DEPARTURE_DATE));
//        String returnDate = cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.RETURN_DATE));
//        int adultCount = cursor.getInt(cursor.getColumnIndex(BudgetsTableEntries.ADULTS_COUNT));
//        int childrenCount = cursor.getInt(cursor.getColumnIndex(BudgetsTableEntries.CHILDREN_COUNT));
//		Budget budget = new Budget(title, plannedBudget, actualBudget, departureDate, adultCount, childrenCount);
		String gender = cursor.getString(cursor.getColumnIndex(BudgetsTableEntries.GENDER));
		Budget budget = new Budget(title, plannedBudget, actualBudget, departureDate, gender);

        budget.setId(id);
		return budget;
	}

    static Category convertCategoryFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(CategoriesTableEntries._ID));
        String title = cursor.getString(cursor.getColumnIndex(CategoriesTableEntries.TITLE));
        BigDecimal plannedCategory = new BigDecimal(cursor.getString(cursor.getColumnIndex(CategoriesTableEntries.PLANNED_CATEGORY)));
        BigDecimal actualCategory = new BigDecimal(cursor.getString(cursor.getColumnIndex(CategoriesTableEntries.ACTUAL_CATEGORY)));
        int backgroundId = cursor.getInt(cursor.getColumnIndex(CategoriesTableEntries.BACKGROUND_ID));
        int budgetId = cursor.getInt(cursor.getColumnIndex(CategoriesTableEntries.BUDGET_ID));

        Category category = new Category(title, plannedCategory, actualCategory, backgroundId,budgetId);
        category.setId(id);
        return category;
    }
}

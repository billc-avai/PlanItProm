package com.sevendesign.planitprom.database.models;

import android.content.ContentValues;
import com.sevendesign.planitprom.database.tables.CategoriesTableEntries;

import java.math.BigDecimal;

public class Category implements IContentValuesConvertable {

    private int id;
    private String title;
    private BigDecimal plannedCategory;
    private BigDecimal actualCategory;
    private int backgroundId;
    private int budgetId;

	public Category(String title, BigDecimal plannedCategory, BigDecimal actualCategory, int backgroundId, int budgetId) {
		this.title = title;
        this.plannedCategory = plannedCategory;
        this.actualCategory = actualCategory;
        this.backgroundId = backgroundId;
        this.budgetId = budgetId;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(CategoriesTableEntries.TITLE, title);
        cv.put(CategoriesTableEntries.PLANNED_CATEGORY, plannedCategory.toPlainString());
        cv.put(CategoriesTableEntries.ACTUAL_CATEGORY, actualCategory.toPlainString());
        cv.put(CategoriesTableEntries.BACKGROUND_ID, backgroundId);
        cv.put(CategoriesTableEntries.BUDGET_ID, budgetId);
		return cv;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public BigDecimal getPlannedCategory() {
        return plannedCategory;
    }
    public void setPlannedCategory(BigDecimal plannedCategory) {
        this.plannedCategory = plannedCategory;
    }
    public BigDecimal getActualCategory() {
        return actualCategory;
    }
    public void setActualCategory(BigDecimal actualCategory) {
        this.actualCategory = actualCategory;
    }
    public int getBudgetId() {
        return budgetId;
    }
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }
}

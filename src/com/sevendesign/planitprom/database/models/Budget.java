package com.sevendesign.planitprom.database.models;

import java.math.BigDecimal;

import android.content.ContentValues;

import com.sevendesign.planitprom.database.tables.BudgetsTableEntries;

public class Budget implements IContentValuesConvertable {
	private int id;
    private String title;
    private BigDecimal plannedBudget;
	private BigDecimal actualBudget;

    private String departureDate;
	private String gender;
	
	public Budget(String title, BigDecimal plannedBudget, BigDecimal actualBudget, String departureDate, String gender) {
		this.title = title;
		this.plannedBudget = plannedBudget;
		this.actualBudget = actualBudget;
		this.departureDate = departureDate;
		this.gender = gender;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(BudgetsTableEntries.TITLE, title);
		cv.put(BudgetsTableEntries.PLANNED_BUDGET, plannedBudget.toPlainString());
		cv.put(BudgetsTableEntries.ACTUAL_BUDGET, actualBudget.toPlainString());
		cv.put(BudgetsTableEntries.DEPARTURE_DATE, departureDate);
		cv.put(BudgetsTableEntries.GENDER, gender);
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
    public BigDecimal getPlannedBudget() {
        return plannedBudget;
    }
    public void setPlannedBudget(BigDecimal plannedBudget) {
        this.plannedBudget = plannedBudget;
    }
    public BigDecimal getActualBudget() {
        return actualBudget;
    }
    public void setActualBudget(BigDecimal actualBudget) {
        this.actualBudget = actualBudget;
    }

    public String getEventDate() {
        return departureDate;
    }
    public void setEventDate(String departureDate) {
        this.departureDate = departureDate;
    }
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGender() {
		return gender;
	}
}

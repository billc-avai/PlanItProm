package com.sevendesign.planitprom.database.models;

import java.math.BigDecimal;

import android.content.ContentValues;

import com.sevendesign.planitprom.database.tables.BudgetsTableEntries;

public class Budget implements IContentValuesConvertable {
	private int id;
    private String title;
    private BigDecimal plannedBudget;
	private BigDecimal actualBudget;
//	private String travelPeriod;
    private String departureDate;
//    private String returnDate;
//    private int adultCount;
//    private int childrenCount;
	private String gender;
	
//	public static final String GENDER_MALE = "Male";
//	public static final String GENDER_FEMALE = "Female";

//    public Budget(String title, BigDecimal plannedBudget, BigDecimal actualBudget, String travelPeriod, String departureDate, String returnDate, int adultCount, int childrenCount) {
	public Budget(String title, BigDecimal plannedBudget, BigDecimal actualBudget, String departureDate, String gender) {
		this.title = title;
		this.plannedBudget = plannedBudget;
		this.actualBudget = actualBudget;
//		this.travelPeriod = travelPeriod;
		this.departureDate = departureDate;
//        this.returnDate = returnDate;
//        this.adultCount = adultCount;
//        this.childrenCount = childrenCount;
		this.gender = gender;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(BudgetsTableEntries.TITLE, title);
		cv.put(BudgetsTableEntries.PLANNED_BUDGET, plannedBudget.toPlainString());
		cv.put(BudgetsTableEntries.ACTUAL_BUDGET, actualBudget.toPlainString());
//		cv.put(BudgetsTableEntries.TRAVEL_PERIOD, travelPeriod);
		cv.put(BudgetsTableEntries.DEPARTURE_DATE, departureDate);
//		cv.put(BudgetsTableEntries.RETURN_DATE, returnDate);
//        cv.put(BudgetsTableEntries.ADULTS_COUNT, adultCount);
//        cv.put(BudgetsTableEntries.CHILDREN_COUNT, childrenCount);
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
	
//    public String getTravelPeriod() {
//        return travelPeriod;
//    }
//    public void setTravelPeriod(String travelPeriod) {
//        this.travelPeriod = travelPeriod;
//    }
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

//    public String getReturnDate() {
//        return returnDate;
//    }
//    public void setReturnDate(String returnDate) {
//        this.returnDate = returnDate;
//    }
//    public int getAdultCount() {
//        return adultCount;
//    }
//    public void setAdultCount(int adultCount) {
//        this.adultCount = adultCount;
//    }
//    public int getChildrenCount() {
//        return childrenCount;
//    }
//    public void setChildrenCount(int childrenCount) {
//        this.childrenCount = childrenCount;
//    }
}

package com.sevendesign.planitprom.ui.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.TimelineAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.models.TimelineCategory;
import com.sevendesign.planitprom.models.TimelinePoint;
import com.sevendesign.planitprom.utils.CalendarUtils;

public class TimelineFragment extends Fragment {
    public static final String TAG = "TimelineFragment";

	private String gender;
    private ListView categoriesList;
    private List<TimelineCategory> categories;
    private TimelineAdapter timelineAdapter;
    private DbManager dbManager = new DbManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		gender = getBudgetGender();//need to call this first because categories require gender
        categories = getCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, null);
        initActionBar();
        initViews(view);
        fillList();
        return view;
    }

    private void initViews(View rootView) {
        categoriesList = (ListView) rootView.findViewById(R.id.timeline_list);
    }

    private void fillList() {
        timelineAdapter = new TimelineAdapter(getActivity(), categories);
        categoriesList.setAdapter(timelineAdapter);
    }
	
	private String getBudgetGender() {
		List<Budget> budgets = new DbManager().getBudgets();
		Budget budget = null;
		String gender = null;
		
		if (budgets.size() > 0) {
			budget = budgets.get(0);
		}
		
		if (budget != null) {
			gender = budget.getGender().toLowerCase();
		}
		
		if (gender == null || gender.length() == 0) {
			gender = "female"; //use female as default here
		}
		
		return gender;
	}

    private List<TimelineCategory> getCategories() {
        List<TimelineCategory> categories = new ArrayList<TimelineCategory>();
        Resources res = getResources();

        // three months category
        String threeMonthsName = res.getString(R.string.timeline_category_three_months);
		String[] threeMonthsArray = res.getStringArray(res.getIdentifier("timeline_point_three_months_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> threeMonthsPoints = new ArrayList<TimelinePoint>(threeMonthsArray.length);
        for (String pointName: threeMonthsArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            threeMonthsPoints.add(point);
        }
        TimelineCategory threeMonthsCategory = new TimelineCategory();
        threeMonthsCategory.setName(threeMonthsName);
        threeMonthsCategory.setPointsList(threeMonthsPoints);

        // two months category
        String twoMonthsName = res.getString(R.string.timeline_category_two_months);
		String[] twoMonthsArray = res.getStringArray(res.getIdentifier("timeline_point_two_months_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> twoMonthsPoints = new ArrayList<TimelinePoint>(twoMonthsArray.length);
        for (String pointName: twoMonthsArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            twoMonthsPoints.add(point);
        }
        TimelineCategory twoMonthsCategory = new TimelineCategory();
        twoMonthsCategory.setName(twoMonthsName);
        twoMonthsCategory.setPointsList(twoMonthsPoints);

        // one month category
        String oneMonthName = res.getString(R.string.timeline_category_one_month);
		String[] oneMonthArray = res.getStringArray(res.getIdentifier("timeline_point_one_month_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> oneMonthPoints = new ArrayList<TimelinePoint>(oneMonthArray.length);
        for (String pointName: oneMonthArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            oneMonthPoints.add(point);
        }
        TimelineCategory oneMonthCategory = new TimelineCategory();
        oneMonthCategory.setName(oneMonthName);
        oneMonthCategory.setPointsList(oneMonthPoints);

        // one three weeks
        String threeWeeksName = res.getString(R.string.timeline_category_three_weeks);
		String[] threeWeeksArray = res.getStringArray(res.getIdentifier("timeline_point_three_weeks_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> threeWeeksPoints = new ArrayList<TimelinePoint>(threeWeeksArray.length);
        for (String pointName: threeWeeksArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            threeWeeksPoints.add(point);
        }
        TimelineCategory threeWeeksCategory = new TimelineCategory();
        threeWeeksCategory.setName(threeWeeksName);
        threeWeeksCategory.setPointsList(threeWeeksPoints);

        // two weeks category
        String twoWeeksName = res.getString(R.string.timeline_category_two_weeks);
		String[] twoWeeksArray = res.getStringArray(res.getIdentifier("timeline_point_two_weeks_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> twoWeeksPoints = new ArrayList<TimelinePoint>(twoWeeksArray.length);
        for (String pointName: twoWeeksArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            twoWeeksPoints.add(point);
        }
        TimelineCategory twoWeeksCategory = new TimelineCategory();
        twoWeeksCategory.setName(twoWeeksName);
        twoWeeksCategory.setPointsList(twoWeeksPoints);

        // one week category
        String oneWeekName = res.getString(R.string.timeline_category_one_week);
		String[] oneWeekArray = res.getStringArray(res.getIdentifier("timeline_point_one_week_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> oneweekPoints = new ArrayList<TimelinePoint>(oneWeekArray.length);
        for (String pointName: oneWeekArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            oneweekPoints.add(point);
        }
        TimelineCategory oneWeekCategory = new TimelineCategory();
        oneWeekCategory.setName(oneWeekName);
        oneWeekCategory.setPointsList(oneweekPoints);

        // day before category
        String dayName = res.getString(R.string.timeline_category_day);
		String[] dayArray = res.getStringArray(res.getIdentifier("timeline_point_day_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> dayPoints = new ArrayList<TimelinePoint>(dayArray.length);
        for (String pointName: dayArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            dayPoints.add(point);
        }
        TimelineCategory dayCategory = new TimelineCategory();
        dayCategory.setName(dayName);
        dayCategory.setPointsList(dayPoints);

        // day of category
        String dayOfName = res.getString(R.string.timeline_category_day_of);
		String[] dayOfArray = res.getStringArray(res.getIdentifier("timeline_point_day_of_" + gender, "array", getActivity().getPackageName()));
        ArrayList<TimelinePoint> dayOfPoints = new ArrayList<TimelinePoint>(dayOfArray.length);
        for (String pointName: dayOfArray) {
            TimelinePoint point = new TimelinePoint();
            point.setName(pointName);
            dayOfPoints.add(point);
        }
        TimelineCategory dayOfCategory = new TimelineCategory();
        dayOfCategory.setName(dayOfName);
        dayOfCategory.setPointsList(dayOfPoints);


        if(dbManager.getBudgets().size() > 0) {
            Budget budget = dbManager.getBudgets().get(0);
            Calendar depCalendar = Calendar.getInstance();
            depCalendar.setTimeInMillis(Long.parseLong(budget.getEventDate()));
            // add all categories
            if(CalendarUtils.weHaveThisTime(3, 0, -1, depCalendar)) {
                categories.add(threeMonthsCategory);
                categories.add(twoMonthsCategory);
                categories.add(oneMonthCategory);
                categories.add(threeWeeksCategory);
                categories.add(twoWeeksCategory);
                categories.add(oneWeekCategory);
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else if(CalendarUtils.weHaveThisTime(2, 0, -1, depCalendar)) {
                categories.add(twoMonthsCategory);
                categories.add(oneMonthCategory);
                categories.add(threeWeeksCategory);
                categories.add(twoWeeksCategory);
                categories.add(oneWeekCategory);
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else if(CalendarUtils.weHaveThisTime(1, 0, -1, depCalendar)) {
                categories.add(oneMonthCategory);
                categories.add(threeWeeksCategory);
                categories.add(twoWeeksCategory);
                categories.add(oneWeekCategory);
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else if(CalendarUtils.weHaveThisTime(0, 3, -1, depCalendar)) {
                categories.add(threeWeeksCategory);
                categories.add(twoWeeksCategory);
                categories.add(oneWeekCategory);
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else if(CalendarUtils.weHaveThisTime(0, 2, -1, depCalendar)) {
                categories.add(twoWeeksCategory);
                categories.add(oneWeekCategory);
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else if(CalendarUtils.weHaveThisTime(0, 1, -1, depCalendar)) {
                categories.add(oneWeekCategory);
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else if(CalendarUtils.weHaveThisTime(0,0,1, depCalendar)) {
                categories.add(dayCategory);
                categories.add(dayOfCategory);
            } else {
                categories.add(dayOfCategory);
            }
        }

        return categories;
    }

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        actionBar.setCustomView(null);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}




















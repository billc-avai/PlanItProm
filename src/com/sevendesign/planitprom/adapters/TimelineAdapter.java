package com.sevendesign.planitprom.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.models.TimelineCategory;
import com.sevendesign.planitprom.models.TimelineItem;
import com.sevendesign.planitprom.models.TimelinePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mib on 20.08.13.
 */
public class TimelineAdapter extends BaseAdapter {

    public static final String DATA_NAME = "TIMELINE_LIST";
    private List<TimelineItem> itemsList;
    private Activity activity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LayoutInflater inflater;

    public TimelineAdapter(Activity activity, List<TimelineCategory> categoriesList) {
        super();
        this.activity = activity;
        this.itemsList = new ArrayList<TimelineItem>();

        preferences = activity.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        inflater = activity.getLayoutInflater();

        fillList(categoriesList);
    }

    private void fillList(List<TimelineCategory> categoriesList) {
        for (TimelineCategory category : categoriesList) {
            itemsList.add(category);
            itemsList.addAll(category.getPointsList());
        }
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public TimelineItem getItem(int position) {
        return itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView titleText;
        CheckBox pointCheck = null;

        TimelineItem item = getItem(position);
        boolean isPoint = item instanceof TimelinePoint;

        if (isPoint) {
            // create point
            convertView = inflater.inflate(R.layout.timeline_list_point_item, parent, false);
            pointCheck = (CheckBox) convertView.findViewById(R.id.timeline_list_point_check);
            titleText = (TextView) convertView.findViewById(R.id.timeline_list_point_title_text);
        } else {
            // create category
            convertView = inflater.inflate(R.layout.timeline_list_category_item, parent, false);
            titleText = (TextView) convertView.findViewById(R.id.timeline_list_category_title_text);
        }

        titleText.setText(item.getName());

        final TextView textViewToUpdate = titleText;
        if (isPoint) {
            final TimelinePoint point = (TimelinePoint) item;

            boolean isChecked = preferences.getBoolean(getPrefsKey(point), false);
            point.setChecked(isChecked);

            pointCheck.setChecked(point.isChecked());
            updatePointTitleColor(textViewToUpdate, point);

            pointCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    point.setChecked(isChecked);
                    updatePointTitleColor(textViewToUpdate, point);

                    editor.putBoolean(getPrefsKey(point), isChecked);
                    editor.commit();

                }
            });
        }

        return convertView;
    }

    private String getPrefsKey(TimelinePoint point) {
        return String.valueOf(point.getName().hashCode());
    }

    private void updatePointTitleColor(TextView titleText, TimelinePoint point) {
        Resources res = activity.getResources();
        if (point.isChecked()) {
            titleText.setTextColor(res.getColor(R.color.item_text_not_active));
        } else {
            titleText.setTextColor(res.getColor(R.color.message_text));
        }
    }

}

package com.sevendesign.planitprom.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.models.TipItem;

/**
 * Created by mib on 21.08.13.
 */
public class TipDetailsAdapter extends BaseAdapter {

	private List<TipItem> tipsDetails;
    private Activity activity;

	public TipDetailsAdapter(Activity activity, List<TipItem> tipsDetails) {
        super();
        this.activity = activity;
        this.tipsDetails = tipsDetails;
    }

    @Override
    public int getCount() {
        return tipsDetails.size();
    }

    @Override
	public TipItem getItem(int position) {
        return tipsDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		TipItem tipItem = getItem(position);
        View rootView = convertView;
        if (convertView == null) {
            rootView = View.inflate(activity, R.layout.tip_details_list_item, null);
        }

		TextView tipTitle = (TextView) rootView.findViewById(R.id.tip_details_list_item_title);
		tipTitle.setText(tipItem.getTitle());

		TextView tipContent = (TextView) rootView.findViewById(R.id.tip_details_list_item_content);
		tipContent.setText(tipItem.getContent());

        return rootView;
    }

}

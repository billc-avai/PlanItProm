package com.sevendesign.planitprom.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.models.TipCategory;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mib on 20.08.13.
 */
public class TipsAdapter extends BaseAdapter {

    private List<TipCategory> tipsList;
    private Activity activity;

    public TipsAdapter(Activity activity, List<TipCategory> tips) {
        super();
        this.activity = activity;
        this.tipsList = tips;
    }

    @Override
    public int getCount() {
        return tipsList.size();
    }

    @Override
    public TipCategory getItem(int position) {
        return tipsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TipCategory tipItem = tipsList.get(position);
        View rootView = convertView;
        if (convertView == null) {
            rootView = View.inflate(activity, R.layout.tips_tip_item, null);
        }

        Button tipButton = (Button) rootView.findViewById(R.id.tips_tip_button);
        tipButton.setText(tipItem.getName());
        tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFragmentAction action = ShowFragmentAction.TIP_DETAILS;
                action.setData(tipItem);
                action.setAddBackStack(true);
                EventBus.getDefault().post(action);
            }
        });

        return rootView;
    }

}

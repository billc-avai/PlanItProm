package com.sevendesign.planitprom.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.models.ThemeItem;
import com.sevendesign.planitprom.models.ThemeListItemModel;

import java.util.ArrayList;

/**
 * Created by mib on 08.08.13.
 */
public class ThemeAdapter extends BaseAdapter {

    private ArrayList<ThemeListItemModel> themeModelList;
    private Activity activity;
    private View.OnClickListener clickListener;
    private int cellWidth;
    private int cellHeight;

    public class ViewHolder {
        public LinearLayout leftContainer;
        public LinearLayout centerContainer;
        public LinearLayout rightContainer;
        public ImageView leftImage;
        public ImageView centerImage;
        public ImageView rightImage;
    }

    public ThemeAdapter(Activity activity, View.OnClickListener clickListener, ArrayList<ThemeItem> themeList,  int cellWidth, int cellHeight) {
        super();
        this.activity = activity;
        this.clickListener = clickListener;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        themeModelList = new ArrayList<ThemeListItemModel>();
        fillThemeItemList(themeList);
    }

    private void fillThemeItemList(ArrayList<ThemeItem> themeList) {
        int i = 0;
        ThemeListItemModel themeModel = null;
        for (ThemeItem theme: themeList) {
            if (i == 0) {
                themeModel = new ThemeListItemModel();
                themeModelList.add(themeModel);
                themeModel.setLeftItem(theme);
                i++;
            } else if (i == 1) {
                themeModel.setCenterItem(theme);
                i++;
            } else if (i == 2) {
                themeModel.setRightItem(theme);
                i = 0;
            }
        }
    }

    @Override
    public int getCount() {
        return themeModelList.size();
    }

    @Override
    public ThemeListItemModel getItem(int position) {
        return themeModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.theme_setup_item, null);

            LinearLayout leftContainer = (LinearLayout) convertView.findViewById(R.id.theme_item_left_container);
            LinearLayout centerContainer = (LinearLayout) convertView.findViewById(R.id.theme_item_center_container);
            LinearLayout rightContainer = (LinearLayout) convertView.findViewById(R.id.theme_item_right_container);

            ImageView leftImage = (ImageView) convertView.findViewById(R.id.theme_item_left_image);
            ImageView centerImage = (ImageView) convertView.findViewById(R.id.theme_item_center_image);
            ImageView rightImage = (ImageView) convertView.findViewById(R.id.theme_item_right_image);

            LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) leftImage.getLayoutParams();
            leftParams.width = cellWidth;
            leftParams.height = cellHeight;

            LinearLayout.LayoutParams centerParams = (LinearLayout.LayoutParams) centerImage.getLayoutParams();
            centerParams.width = cellWidth;
            centerParams.height = cellHeight;

            LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) rightImage.getLayoutParams();
            rightParams.width = cellWidth;
            rightParams.height = cellHeight;

            leftImage.setLayoutParams(leftParams);
            centerImage.setLayoutParams(centerParams);
            rightImage.setLayoutParams(rightParams);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.leftContainer = leftContainer;
            viewHolder.centerContainer = centerContainer;
            viewHolder.rightContainer = rightContainer;
            viewHolder.leftImage = leftImage;
            viewHolder.centerImage = centerImage;
            viewHolder.rightImage = rightImage;
            convertView.setTag(viewHolder);
        }

        ThemeListItemModel item = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        ThemeItem leftItem = item.getLeftItem();
        ThemeItem centerItem = item.getCenterItem();
        ThemeItem rightItem = item.getRightItem();

        if (leftItem != null) {
            viewHolder.leftContainer.setVisibility(View.VISIBLE);
            viewHolder.leftContainer.setOnClickListener(clickListener);
            viewHolder.leftImage.setImageResource(leftItem.getPreviewId());
            viewHolder.leftContainer.setTag(leftItem);
        } else {
             viewHolder.leftContainer.setVisibility(View.INVISIBLE);
             viewHolder.leftContainer.setOnClickListener(null);
             viewHolder.leftImage.setImageBitmap(null);
        }
        if (centerItem != null) {
            viewHolder.centerContainer.setVisibility(View.VISIBLE);
            viewHolder.centerContainer.setOnClickListener(clickListener);
            viewHolder.centerImage.setImageResource(centerItem.getPreviewId());
            viewHolder.centerContainer.setTag(centerItem);
        } else {
            viewHolder.centerContainer.setVisibility(View.INVISIBLE);
            viewHolder.centerContainer.setOnClickListener(null);
            viewHolder.centerImage.setImageBitmap(null);
        }
        if (rightItem != null) {
            viewHolder.rightContainer.setVisibility(View.VISIBLE);
            viewHolder.rightContainer.setOnClickListener(clickListener);
            viewHolder.rightImage.setImageResource(rightItem.getPreviewId());
            viewHolder.rightContainer.setTag(rightItem);
        } else {
            viewHolder.rightContainer.setVisibility(View.INVISIBLE);
            viewHolder.rightContainer.setOnClickListener(null);
            viewHolder.rightImage.setImageBitmap(null);
        }

        return convertView;
    }

}

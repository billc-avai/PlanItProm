package com.sevendesign.planitprom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.utils.PhotoUtils;

import java.util.List;

/**
 * Created by mib on 08.08.13.
 */
public class GalleryAdapter extends BaseAdapter {

    private int cellWidth;
    private int cellHeight;
    private List<PhotoItem> galleryItemList;
    private Activity activity;

    public GalleryAdapter(Activity activity, List<PhotoItem> galleryItemList, int cellWidth, int cellHeight) {
        super();
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.activity = activity;
        this.galleryItemList = galleryItemList;
    }

    @Override
    public int getCount() {
        return galleryItemList.size();
    }

    @Override
    public PhotoItem getItem(int position) {
        return galleryItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(activity);
            rootView = li.inflate(R.layout.gallery_item, null);
        }

        ImageView iv = (ImageView) rootView.findViewById(R.id.gallery_item_image);
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.width = cellWidth;
        lp.height = cellHeight;

        PhotoItem photoItem = getItem(position);
        iv.setImageBitmap(PhotoUtils.getPhotoBitmap(cellWidth, cellHeight, photoItem.getPath()));
        //iv.setTag(id);

        return rootView;
    }

}

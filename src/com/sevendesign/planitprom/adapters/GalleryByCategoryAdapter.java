package com.sevendesign.planitprom.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;

/**
 * Created by mib on 08.08.13.
 */
public class GalleryByCategoryAdapter extends BaseExpandableListAdapter {
	private int cellWidth;
    private int cellHeight;
	private List<Category> categories;
	private SparseArray<List<PhotoItem>> photoItemMap;
    private Activity activity;

	public GalleryByCategoryAdapter(Activity activity, List<Category> categories, SparseArray<List<PhotoItem>> photoItemMap, int cellWidth, int cellHeight) {
        super();
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.activity = activity;
		this.categories = categories;
		this.photoItemMap = photoItemMap;
    }
	

	public List<PhotoItem> getPhotoItemsByCategoryIndex(int index) {
		if (categories == null) {
			return null;
		}
		
		Category category = categories.get(index);
		List<PhotoItem> photoItems = null;
		
		if (category != null && photoItemMap != null) {
			photoItems = photoItemMap.get(category.getId());
		}
		
		return photoItems;
	}



	@Override
	public int getGroupCount() {
		if (categories != null) {
			return categories.size();
		}
		
		return 0;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		if (categories == null) {
			return null;
		}

		return categories.get(groupPosition);
	}
	
	@Override
	public PhotoItem getChild(int groupPosition, int childPosition) {
		List<PhotoItem> photoItems = getPhotoItemsByCategoryIndex(groupPosition);
		
		PhotoItem item = null;
		
		if (photoItems != null) {
			item = photoItems.get(childPosition);
		}
		
		return item;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		if (convertView instanceof View)
		{
			return (View) convertView;
		}
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View item = (View) inflater.inflate(R.layout.gallery_category_item, null);
		
		TextView text = (TextView) item.findViewById(R.id.category_item_title_text);
		text.setText(((Category) getGroup(groupPosition)).getTitle());
		return item;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		GridView grid = (GridView) convertView;
		
		if (grid == null) {
			LayoutInflater li = LayoutInflater.from(activity);
			grid = (GridView) li.inflate(R.layout.fragment_gallery, null);
		}
		
		grid.setAdapter(new GalleryAdapter(activity, getPhotoItemsByCategoryIndex(groupPosition), cellWidth, cellHeight));
		
		int photoCount = photoItemMap.get(categories.get(groupPosition).getId()).size();
		int rowHeight = cellHeight * ((photoCount / 3) + ((photoCount % 3) > 0 ? 1 : 0));
		
		grid.setLayoutParams(new AbsListView.LayoutParams(parent.getLayoutParams().width, rowHeight));
		
		return grid;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}

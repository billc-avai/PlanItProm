package com.sevendesign.planitprom.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.GalleryByCategoryAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.utils.KWFileUtils;
import com.sevendesign.planitprom.utils.PhotoUtils;
import com.sevendesign.planitprom.utils.Utils;

public class GalleryByCategoryFragment extends Fragment {
	public static final String TAG = "GalleryByCategoryFragment";
	private List<Category> categories;
	private List<Category> prunedCategories;
	private SparseArray<List<PhotoItem>> photoItems;
    private ExpandableListView galleryCategoryList;
	private Button takePictureButton;
	private GalleryByCategoryAdapter adapter;
    private DbManager manager = new DbManager();
	private String photoPath = "";
	private int categoryForPendingPhoto;
	private int cellWidth;
	private static final int CELL_NUMBER = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gallery_by_category, null);
        initActionBar();
        initViews(view);
        initContent();
        return view;
    }

    private void initViews(View rootView) {
		galleryCategoryList = (ExpandableListView) rootView.findViewById(R.id.gallery_expandable_list);
		takePictureButton = (Button) rootView.findViewById(R.id.photo_button);
		
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.alert_select_category_title);
				
				String[] dialogList = new String[categories.size()];
				
				for (int i = 0; i < categories.size(); i++) {
					dialogList[i] = categories.get(i).getTitle();
				}
				
				builder.setItems(dialogList, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						categoryForPendingPhoto = categories.get(which).getId();
						photoPath = KWFileUtils.getDestinationPath(getActivity(), PhotoUtils.getPhotoName(), PhotoUtils.JPEG_FILE_SUFFIX);
						PhotoUtils.takePhoto(GalleryByCategoryFragment.this, photoPath);
						
					}
				});
				
				builder.show();

			}
		});
    }

//    private AdapterView.OnItemClickListener themeClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			PhotoItem item = adapter.getItem(position);
//            ShowFragmentAction action = ShowFragmentAction.GALLERY_DETAILS;
//            action.setAddBackStack(true);
//            action.setData(item);
//            EventBus.getDefault().post(action);
//        }
//    };
	
	private void refreshContent() {
		prunedCategories = new ArrayList<Category>();
		List<PhotoItem> photoItemsAll = manager.getPhotoItems();
		photoItems = new SparseArray<List<PhotoItem>>();
		
		for (Category category : categories) {
			ArrayList<PhotoItem> categoryPhotoItems = new ArrayList<PhotoItem>();
			
			for (PhotoItem photoItem : photoItemsAll) {
				if (photoItem.getCategoryId() == category.getId()) {
					categoryPhotoItems.add(photoItem);
				}
			}
			
			if (categoryPhotoItems.size() > 0) {
				photoItems.put(category.getId(), categoryPhotoItems);
				prunedCategories.add(category);
			}
		}
		
		adapter = new GalleryByCategoryAdapter(getActivity(), prunedCategories, photoItems, cellWidth, cellWidth);
		galleryCategoryList.setAdapter(adapter);

	}

    private void initContent() {
        int displayWidth = Utils.getScreenWidth(getActivity());

        // get dimensions
        Resources res = getActivity().getResources();
        int verticalDivider = res.getDimensionPixelSize(R.dimen.gallery_grid_vertical_divider); // three dividers
        int paddingLeft = res.getDimensionPixelSize(R.dimen.gallery_grid_padding_left);

        // calculate width and height for grid cell
		cellWidth = ((displayWidth - (paddingLeft + verticalDivider) - verticalDivider * (CELL_NUMBER - 1)) / CELL_NUMBER);
		
		int indicatorLeftBound = displayWidth - 70;
		int indicatorRightBound = displayWidth - 20;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			galleryCategoryList.setIndicatorBounds(indicatorLeftBound, indicatorRightBound);
		} else {
			galleryCategoryList.setIndicatorBoundsRelative(indicatorLeftBound, indicatorRightBound);
		}
		
		categories = manager.getCategories(1);
		refreshContent();
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK &&
				(requestCode == PhotoUtils.ACTION_TAKE_PHOTO ||
				requestCode == PhotoUtils.ACTION_PICK_PHOTO)) {
			
			if (categoryForPendingPhoto > 0) {
				PhotoUtils.handlePhotoResult(getActivity(), requestCode, data, categoryForPendingPhoto, photoPath);
				refreshContent();
			}
		}
	}

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        actionBar.setCustomView(null);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}




















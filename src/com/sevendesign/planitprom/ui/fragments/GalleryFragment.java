package com.sevendesign.planitprom.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.GalleryAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.utils.Dialogs;
import com.sevendesign.planitprom.utils.Utils;

import java.util.List;

import de.greenrobot.event.EventBus;

public class GalleryFragment extends Fragment {
    public static final String TAG = "GalleryFragment";
    private GridView galleryGrid;
    private GalleryAdapter galleryAdapter;
    private DbManager manager = new DbManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, null);
        initActionBar();
        initViews(view);
        initContent();
        return view;
    }

    private void initViews(View rootView) {
        galleryGrid = (GridView) rootView.findViewById(R.id.gallery_grid);
    }

    private AdapterView.OnItemClickListener themeClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PhotoItem item = galleryAdapter.getItem(position);
            ShowFragmentAction action = ShowFragmentAction.GALLERY_DETAILS;
            action.setAddBackStack(true);
            action.setData(item);
            EventBus.getDefault().post(action);
        }
    };

    private void initContent() {
        int displayWidth = Utils.getScreenWidth(getActivity());

        // get dimensions
        Resources res = getActivity().getResources();
        int verticalDivider = res.getDimensionPixelSize(R.dimen.gallery_grid_vertical_divider); // three dividers
        int paddingLeft = res.getDimensionPixelSize(R.dimen.gallery_grid_padding_left);

        // calculate width and height for grid cell
        int cellNumber = 3;
        int cellWidth = ((displayWidth - (paddingLeft + verticalDivider) - verticalDivider * (cellNumber - 1)) / cellNumber);
        int cellHeight = cellWidth;

        List<PhotoItem> photoItems = manager.getPhotoItems();
        galleryAdapter = new GalleryAdapter(getActivity(), photoItems, cellWidth, cellHeight);
        galleryGrid.setAdapter(galleryAdapter);
        galleryGrid.setOnItemClickListener(themeClickListener);
    }

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        actionBar.setCustomView(null);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}




















package com.sevendesign.planitprom.ui.fragments;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.ui.activities.FacebookPublisher;
import com.sevendesign.planitprom.utils.PhotoUtils;

public class PhotoFragment extends Fragment {
	public static final String TAG = "PhotoFragment";
    public static final String PHOTO_ITEM = "photo_item";
    private PhotoItem photoItem;
	private TextView photoTitle;
    private ImageView detailsImage;
	private Bitmap photoBitmap;
	private Button shareButton;
    private Button deleteButton;
    private DbManager manager = new DbManager();

    public static PhotoFragment newInstance(PhotoItem item) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putParcelable(PHOTO_ITEM, item);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoItem = getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, null);
        initActionBar(inflater);
        initViews(view);
        initListeners();
        return view;
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
		actionBar.setTitle("Photo Gallery");
    }

    private void initViews(View rootView) {
		photoTitle = (TextView) rootView.findViewById(R.id.photo_title);
        detailsImage = (ImageView) rootView.findViewById(R.id.gallery_details_image);
		shareButton = (Button) rootView.findViewById(R.id.share_button);
        deleteButton = (Button) rootView.findViewById(R.id.gallery_details_delete_button);
    }

    private void initListeners() {
        ViewTreeObserver imageVto = detailsImage.getViewTreeObserver();
        imageVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detailsImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int imageWidth = detailsImage.getMeasuredWidth();

                initContent(imageWidth);
            }
        });

		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sharePhoto(photoItem.getPath());
			}
		});

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.alert_delete_photo_title);
				builder.setPositiveButton(android.R.string.yes, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (photoItem != null) {
							File photo = new File(photoItem.getPath());
							photo.delete();
							manager.removePhotoItem(photoItem);
							getActivity().getSupportFragmentManager().popBackStack();
						}
					}
				});
				
				builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				builder.show();
            }
        });
    }

    private void initContent(int imageWidth) {
        ViewGroup.LayoutParams imageLp = detailsImage.getLayoutParams();
        imageLp.width = imageWidth;
        imageLp.height = imageWidth;

        if (photoItem != null) {
			
			Category category = manager.getCategory(photoItem.getCategoryId());
			if (category != null) {
				String photoLabelText = getActivity().getResources().getString(R.string.category) + ": " + category.getTitle();
				
				photoTitle.setText(photoLabelText);
			} else {
				photoTitle.setVisibility(View.GONE);
			}

			photoBitmap = PhotoUtils.getPhotoBitmap(getActivity(), imageWidth, imageWidth, photoItem.getPath());
			detailsImage.setImageBitmap(photoBitmap);
        }
    }
    private PhotoItem getData() {
        Bundle bundle = getArguments();
        PhotoItem item = null;
        if (bundle != null && bundle.containsKey(PHOTO_ITEM)) {
            item = (PhotoItem) bundle.getParcelable(PHOTO_ITEM);
        }
        
        return item;
    }

    private void sharePhoto(String path){
        FacebookPublisher fbPublisher = (FacebookPublisher) getActivity();
        fbPublisher.postPhoto(path);
    }

}
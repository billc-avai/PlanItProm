package com.sevendesign.planitprom.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.ui.activities.FacebookPublisher;
import com.sevendesign.planitprom.utils.BudgetUtils;
import com.sevendesign.planitprom.utils.PhotoUtils;
import com.sevendesign.planitprom.utils.Utils;

import java.io.File;
import java.math.BigDecimal;

public class GalleryDetailsFragment extends Fragment {
    public static final String TAG = "GalleryDetailsFragment";
    public static final String PHOTO_ITEM = "photo_item";
    private PhotoItem photoItem;
    private ImageView detailsImage;
    private EditText itemNameEdit;
    private EditText costEdit;
    private EditText sellerNameEdit;
    private EditText notesEdit;
    private EditText dateEdit;
    private Button deleteButton;
    private DbManager manager = new DbManager();
    private int currencyMaxLength;

    public static GalleryDetailsFragment newInstance(PhotoItem item) {
        GalleryDetailsFragment fragment = new GalleryDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(PHOTO_ITEM, item);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoItem = getData();
        currencyMaxLength = getResources().getInteger(R.integer.currency_max_length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_details, null);
        initActionBar(inflater);
        initViews(view);
        initListeners();
        return view;
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        View actionView = inflater.inflate(R.layout.action_bar_photo_item, null);

        actionView.findViewById(R.id.shareButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePhoto(photoItem.getPath());
            }
        });
        actionView.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void initViews(View rootView) {
        detailsImage = (ImageView) rootView.findViewById(R.id.gallery_details_image);
        itemNameEdit = (EditText) rootView.findViewById(R.id.gallery_details_item_name_edit);
        costEdit = (EditText) rootView.findViewById(R.id.gallery_details_cost_edit);
        costEdit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        sellerNameEdit = (EditText) rootView.findViewById(R.id.gallery_details_merchant_name_edit);
        notesEdit = (EditText) rootView.findViewById(R.id.gallery_details_notes_edit);
        dateEdit = (EditText) rootView.findViewById(R.id.gallery_details_date_edit);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoItem != null) {
                    File photo = new File(photoItem.getPath());
                    photo.delete();
                    manager.removePhotoItem(photoItem);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void initContent(int imageWidth) {
        ViewGroup.LayoutParams imageLp = detailsImage.getLayoutParams();
        imageLp.width = imageWidth;
        imageLp.height = imageWidth;

        if (photoItem != null) {
            itemNameEdit.setText(photoItem.getTitle());
            if(photoItem.getCost().compareTo(BigDecimal.ZERO) != 0) {
                costEdit.setText(BudgetUtils.getMoneyValueString(photoItem.getCost()));

            } else {
                costEdit.setText("");
            }
            sellerNameEdit.setText(photoItem.getMerchant());
            notesEdit.setText(photoItem.getNotes());
            dateEdit.setText(photoItem.getDate());
            detailsImage.setImageBitmap(PhotoUtils.getPhotoBitmap(imageWidth, imageWidth, photoItem.getPath()));
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

    private void saveData() {
        BigDecimal cost = BigDecimal.ZERO;
        try {
            if(!costEdit.getText().toString().equalsIgnoreCase("")) {
                cost = BudgetUtils.getMoneyValueFromString(costEdit.getText().toString());
            }
        } catch (Exception exc){}
        photoItem.setTitle(itemNameEdit.getText().toString());
        photoItem.setCost(cost);
        photoItem.setMerchant(sellerNameEdit.getText().toString());
        photoItem.setNotes(notesEdit.getText().toString());

        manager.updatePhotoItem(photoItem);
    }
}
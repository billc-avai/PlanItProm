package com.sevendesign.planitprom.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.utils.BudgetUtils;
import com.sevendesign.planitprom.utils.KWFileUtils;
import com.sevendesign.planitprom.utils.PhotoUtils;
import com.sevendesign.planitprom.utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;

public class CategoryAddFragment extends Fragment {

    public static final String TAG = "CategoryAddFragment";
    public static final String ITEM_NAME = "item_name";

    private TextView remainBudgetText;
    private EditText itemNameEdit;
    private EditText budgetedEdit;
    private EditText actualEdit;
    private Button takePictureButton;
    private Button deleteCategory;
    private final DbManager dbManager = new DbManager();
    private Category category;
    private String photoPath = "";
    public static final String PHOTO_PATH = "photo_path";
    private int currencyMaxLength;

    public static CategoryAddFragment newInstance(long id) {
        CategoryAddFragment fragment = new CategoryAddFragment();
        Bundle args = new Bundle();
        args.putLong(ITEM_NAME, id);
        fragment.setArguments(args);

        return fragment;
    }

    /* returns item name */
    private long getData() {
        Bundle bundle = getArguments();
        long itemId = -1;
        if (bundle != null && bundle.containsKey(ITEM_NAME)) {
            itemId = bundle.getLong(ITEM_NAME);
        }
        return itemId;
    }

    private String getPhotoPath(Bundle savedInstanceState) {
        String path = "";
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(PHOTO_PATH);
        }
        return path;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoPath = getPhotoPath(savedInstanceState);
        currencyMaxLength = getResources().getInteger(R.integer.currency_max_length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_add, null);
        initViews(view);
        initListeners();
        initActionBar(inflater);
        fillContent();

        return view;
    }

    private void initViews(View root) {
        remainBudgetText = (TextView) root.findViewById(R.id.category_add_remain_budget_text);
        itemNameEdit = (EditText) root.findViewById(R.id.category_add_item_name_edit);
        budgetedEdit = (EditText) root.findViewById(R.id.category_add_budgeted_edit);
        budgetedEdit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        actualEdit = (EditText) root.findViewById(R.id.category_add_actual_edit);
        actualEdit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        takePictureButton = (Button) root.findViewById(R.id.category_add_take_picture_button);
        deleteCategory= (Button) root.findViewById(R.id.category_add_delete_button);
    }

    private void initListeners() {
        final EditText actualE = actualEdit;
        final EditText budgetedE = budgetedEdit;
        budgetedEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utils.updateTextColor(actualE, budgetedE, getResources());
            }
        });

        actualEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utils.updateTextColor(actualE, budgetedE, getResources());
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPath = KWFileUtils.getDestinationPath(getActivity(), PhotoUtils.getPhotoName(), PhotoUtils.JPEG_FILE_SUFFIX);
                PhotoUtils.takePhoto(CategoryAddFragment.this, photoPath);
            }
        });

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            long id = getData();
            if(id > -1) {
                dbManager.deleteCategory(id);
                getActivity().getSupportFragmentManager().popBackStack();
            }
            }
        });
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        View actionView = inflater.inflate(R.layout.action_bar_category_add, null);
        actionView.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                saveData();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void fillContent() {
        long itemId = getData();
        category = dbManager.getCategory(itemId);
        if(category != null) {
            Budget budget = dbManager.getBudget(category.getBudgetId());
            if(budget != null) {
                BigDecimal budgetRemaining = budget.getPlannedBudget().subtract(budget.getActualBudget());
                String currency = getString(R.string.prom_budget_currency);
                StringBuilder remainSB = new StringBuilder(currency);
                remainSB.append(BudgetUtils.getMoneyValueString(budgetRemaining));
                remainBudgetText.setText(remainSB.toString());
                if(category != null) {
                    itemNameEdit.setText(category.getTitle());
                    if(category.getPlannedCategory().compareTo(BigDecimal.ZERO) != 0) {
                        budgetedEdit.setText(BudgetUtils.getMoneyValueString(category.getPlannedCategory()));
                    }
                    if(category.getActualCategory().compareTo(BigDecimal.ZERO) != 0) {
                        actualEdit.setText(BudgetUtils.getMoneyValueString(category.getActualCategory()));
                    }
                }
            }
        }
    }

    private void saveData() {
        Category category = dbManager.getCategory(getData());

        if(budgetedEdit.getText().toString().equalsIgnoreCase("")) {
            category.setPlannedCategory(BigDecimal.ZERO);
        } else {
            category.setPlannedCategory(BudgetUtils.getMoneyValueFromString(budgetedEdit.getText().toString()));
        }
        if(actualEdit.getText().toString().equalsIgnoreCase("")) {
            category.setActualCategory(BigDecimal.ZERO);
        } else {
            category.setActualCategory(BudgetUtils.getMoneyValueFromString(actualEdit.getText().toString()));
        }

        dbManager.updateCategory(category);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtils.ACTION_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if(category != null) {
                String title = "";
                String merchant = "";
                BigDecimal cost = BigDecimal.ZERO;
                String notes = "";

                Calendar cal = Calendar.getInstance();
                StringBuilder dataBuilder = new StringBuilder();
                dataBuilder.append(String.format("%tB", cal));
                dataBuilder.append(" ");
                dataBuilder.append(cal.get(Calendar.DAY_OF_MONTH));
                dataBuilder.append(", ");
                dataBuilder.append(cal.get(Calendar.YEAR));
                String date = dataBuilder.toString();
                dbManager.addPhotoItem(new PhotoItem(title, photoPath, cost, merchant, notes, date, category.getId()));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PHOTO_PATH, photoPath);
    }

    private void hideKeyboard() {
        Activity activity = getActivity();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){}
    }

}




















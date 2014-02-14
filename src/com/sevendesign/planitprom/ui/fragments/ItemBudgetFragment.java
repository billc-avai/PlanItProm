package com.sevendesign.planitprom.ui.fragments;

import java.math.BigDecimal;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.ItemBudgetAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.database.models.Category;
import com.sevendesign.planitprom.database.models.PhotoItem;
import com.sevendesign.planitprom.ui.widgets.PageControl;
import com.sevendesign.planitprom.utils.BudgetUtils;
import com.sevendesign.planitprom.utils.Dialogs;
import com.sevendesign.planitprom.utils.KWFileUtils;
import com.sevendesign.planitprom.utils.PhotoUtils;
import com.sevendesign.planitprom.utils.Utils;

public class ItemBudgetFragment extends Fragment implements BackButtonListener {
    public static final String TAG = "ItemBudgetFragment";
    public static final String BUDGET_AND_CATEGORY_IDS = "budget_and_category_ids";
    public static final String PHOTO_PATH = "photo_path";

    private TextView titleText;
    private Button takePictureButton;
    private ImageView headerImage;

    private ViewPager counterPager;
    private int counterCurrentPage = 0;
    private PageControl counterPageControl;
    private ItemBudgetAdapter counterAdapter;
    private DbManager manager = new DbManager();

    private EditText budgetedEditText;
    private EditText actualEditText;
    private String photoPath = "";

    private Category category;
    private int currencyMaxLength;

    public static ItemBudgetFragment newInstance(int id) {
        ItemBudgetFragment fragment = new ItemBudgetFragment();
        Bundle args = new Bundle();
        args.putInt(BUDGET_AND_CATEGORY_IDS, id);
        fragment.setArguments(args);

        return fragment;
    }

    private int getCategoryId() {
        Bundle bundle = getArguments();
        int id = -1;
        if (bundle != null) {
            id = bundle.getInt(BUDGET_AND_CATEGORY_IDS);
        }
        return id;
    }

    private String getPhotoPath(Bundle savedInstanceState) {
        String path = "";
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(PHOTO_PATH);
        }
        return path;
    }

    private void initBudget(Category category) {
        Budget budget = new DbManager().getBudget(category.getBudgetId());
        if(budget != null) {
            initPager(budget);
            fillCounterPages();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoPath = getPhotoPath(savedInstanceState);
        currencyMaxLength = getResources().getInteger(R.integer.currency_max_length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_budget, null);
        initActionBar(inflater);
        initViews(view);
        fillViews();
        initListeners();

        return view;
    }

    private void initPager(Budget budget) {
        counterAdapter = new ItemBudgetAdapter(getActivity(), budget);
        counterPager.setAdapter(counterAdapter);
        counterPager.setOnPageChangeListener(new CounterPageChangeListener());
    }

    private void initViews(View view) {
        titleText = (TextView) view.findViewById(R.id.item_budget_title_text);
        takePictureButton = (Button) view.findViewById(R.id.item_budget_take_picture_button);
        counterPager = (ViewPager) view.findViewById(R.id.item_budget_counter_pager);
        counterPageControl = (PageControl) view.findViewById(R.id.item_budget_counter_page_control);
        budgetedEditText = (EditText) view.findViewById(R.id.item_budget_budgeted_edit);
        budgetedEditText.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        actualEditText = (EditText) view.findViewById(R.id.item_budget_actual_edit);
        actualEditText.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        headerImage = (ImageView) view.findViewById(R.id.header_image);
    }

    private void fillViews() {
        int id = getCategoryId();
        category = manager.getCategory(id);
        if(category != null) {
            if(category.getBackgroundId()!=Utils.getCategoryBackgroundDefault()) {
                headerImage.setImageDrawable(getActivity().getResources().getDrawable(category.getBackgroundId()));
            }
            titleText.setText(category.getTitle());

            if(category.getPlannedCategory().compareTo(BigDecimal.ZERO) != 0) {
                budgetedEditText.setText(BudgetUtils.getMoneyValueString(category.getPlannedCategory()));
            }
            if(category.getActualCategory().compareTo(BigDecimal.ZERO) != 0) {
                actualEditText.setText(BudgetUtils.getMoneyValueString(category.getActualCategory()));
            }
            initBudget(category);
        }

        final EditText actualE = actualEditText;
        final EditText budgetedE = budgetedEditText;
        Utils.updateTextColor(actualE, budgetedE, getResources());
    }

    private void initListeners() {
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPath = KWFileUtils.getDestinationPath(getActivity(), PhotoUtils.getPhotoName(), PhotoUtils.JPEG_FILE_SUFFIX);
                PhotoUtils.takePhoto(ItemBudgetFragment.this, photoPath);
            }
        });

        final EditText actualE = actualEditText;
        final EditText budgetedE = budgetedEditText;
        budgetedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Utils.updateTextColor(actualE, budgetedE, getResources());
            }
        });

        actualEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Utils.updateTextColor(actualE, budgetedE, getResources());
            }
        });
    }

    private void fillCounterPages() {
        counterPageControl.setPageCount(2);
		counterPageControl.setActiveDrawable(getResources().getDrawable(R.drawable.bullet_selected));
		counterPageControl.setInactiveDrawable(getResources().getDrawable(R.drawable.bullet_not_selected));
        counterPager.setCurrentItem(counterCurrentPage);
        changeCounterPage(counterCurrentPage);
        counterAdapter.notifyDataSetChanged();
    }

    private void changeCounterPage(int index) {
        counterPageControl.setCurrentPage(index);
    }

    @Override
    public void backButtonPressed() {
        Category category = manager.getCategory(getCategoryId());

        if(budgetedEditText.getText().toString().equalsIgnoreCase("")) {
            category.setPlannedCategory(BigDecimal.ZERO);
        } else {
            category.setPlannedCategory(BudgetUtils.getMoneyValueFromString(budgetedEditText.getText().toString()));
        }

        if(actualEditText.getText().toString().equalsIgnoreCase("")) {
            category.setActualCategory(BigDecimal.ZERO);
        } else {
            category.setActualCategory(BudgetUtils.getMoneyValueFromString(actualEditText.getText().toString()));
        }

        manager.updateCategory(category);
    }

    private class CounterPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) { }

        @Override
        public void onPageSelected(int i) {
            counterCurrentPage = i;
            changeCounterPage(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) { }
    }

    private void showInfoDialog() {
        Dialogs.showInstruction(getActivity(), getString(R.string.alert_dialog_message_item_budget_instructions));
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        View actionView = inflater.inflate(R.layout.action_bar_credit, null);
        actionView.findViewById(R.id.faqButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
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
                manager.addPhotoItem(new PhotoItem(title, photoPath, cost, merchant, notes, date, category.getId()));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PHOTO_PATH, photoPath);
    }
}




















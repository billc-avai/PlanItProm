package com.sevendesign.planitprom.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.utils.Utils;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

public class CalculatorFragment extends Fragment {
    public static final String TAG = "CalculatorFragment";
    private EditText tagPriceEdit;
    private EditText salesTaxEdit;
    private EditText discountEdit;
    private EditText tipEdit;
    private Button calculateButton;
    private EditText totalEdit;
    private Button creditButton;
    private int currencyMaxLength;
    private int percentMaxLength;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        currencyMaxLength = res.getInteger(R.integer.currency_max_length);
        percentMaxLength = res.getInteger(R.integer.percent_max_length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, null);
        initActionBar();
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(View root) {
        tagPriceEdit = (EditText) root.findViewById(R.id.calculator_tag_price_edit);
        tagPriceEdit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        salesTaxEdit = (EditText) root.findViewById(R.id.calculator_sales_tax_edit);
        salesTaxEdit.setFilters(Utils.getCurrencyInputFilters(percentMaxLength));
        discountEdit = (EditText) root.findViewById(R.id.calculator_discount_edit);
        discountEdit.setFilters(Utils.getCurrencyInputFilters(percentMaxLength));
        tipEdit = (EditText) root.findViewById(R.id.calculator_tip_edit);
        calculateButton = (Button) root.findViewById(R.id.calculator_calculate_button);
        totalEdit = (EditText) root.findViewById(R.id.calculator_total_edit);
        totalEdit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        creditButton = (Button) root.findViewById(R.id.calculator_credit_button);
    }

    private void initListeners() {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double total = calculatePrice();
                totalEdit.setText(Utils.getStringPrice(total));
            }
        });
        creditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFragmentAction action = ShowFragmentAction.CREDIT;
                action.setAddBackStack(true);
                EventBus.getDefault().post(ShowFragmentAction.CREDIT);
            }
        });
    }

    private double calculatePrice() {
        String priceString = tagPriceEdit.getText().toString();
        String taxString = salesTaxEdit.getText().toString();
        String discountString = discountEdit.getText().toString();
        String tipString = tipEdit.getText().toString();
        double price = 0d;
        double tax = 0d;
        double discount = 0d;
        double tip = 0d;
        double total = 0d;

        try {
            if (priceString != null && !priceString.equals("")) {
                price = Double.parseDouble(priceString);
            }
            if (taxString != null && !taxString.equals("")) {
                tax = Double.parseDouble(taxString);
            }
            if (discountString != null && !discountString.equals("")) {
                discount = Double.parseDouble(discountString);
            }
            if (tipString != null && !tipString.equals("")) {
                tip = Double.parseDouble(tipString);
            }

            // old calculations
            /*price = price * (1d - discount / 100d);
            total = tax != 0 ? price * (1d - tax / 100d) : price;*/

            total = price * (1d - (discount / 100d)) * (tax / 100d + 1d) + tip;
            if (total < 0) {
                total = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return total;
    }

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        actionBar.setCustomView(null);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}




















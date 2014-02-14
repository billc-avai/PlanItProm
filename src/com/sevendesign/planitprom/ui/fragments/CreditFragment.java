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
import com.sevendesign.planitprom.utils.Dialogs;
import com.sevendesign.planitprom.utils.Utils;

public class CreditFragment extends Fragment {
    public static final String TAG = "CreditFragment";
    private EditText totalCostEdit;
    private EditText itemRateEdit;
    private Button calculateButton;
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
        View view = inflater.inflate(R.layout.fragment_credit, null);
        initActionBar(inflater);
        initViews(view);
        initListeners();
        return view;
    }

    private void initActionBar(LayoutInflater inflater) {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        View actionView = inflater.inflate(R.layout.action_bar_credit, null);
        actionView.findViewById(R.id.faqButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        actionBar.setCustomView(actionView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void initViews(View root) {
        totalCostEdit = (EditText) root.findViewById(R.id.credit_total_cost_edit);
        totalCostEdit.setFilters(Utils.getCurrencyInputFilters(currencyMaxLength));
        itemRateEdit = (EditText) root.findViewById(R.id.credit_card_rate_edit);
        itemRateEdit.setFilters(Utils.getCurrencyInputFilters(percentMaxLength));
        calculateButton = (Button) root.findViewById(R.id.credit_calculate_button);
    }

    private void initListeners() {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalCostString = totalCostEdit.getText().toString();
                String cardRateString = itemRateEdit.getText().toString();
                double totalCost = 0;
                double cardRate = 0;
                if (totalCostString != null && !totalCostString.equals("")) {
                    totalCost = Double.parseDouble(totalCostString);
                }
                if (cardRateString != null && !cardRateString.equals("")) {
                    cardRate = Double.parseDouble(cardRateString);
                }

                double threeMonthsPrice = Utils.calculateCreditPrice(totalCost, cardRate, 3);
                double sixMonthsPrice = Utils.calculateCreditPrice(totalCost, cardRate, 6);
                double twelveMonthsPrice = Utils.calculateCreditPrice(totalCost, cardRate, 12);

                Dialogs.showCredit(getActivity(),
                        Utils.getStringPrice(threeMonthsPrice),
                        Utils.getStringPrice(sixMonthsPrice),
                        Utils.getStringPrice(twelveMonthsPrice));
            }
        });
    }

    private void showDialog() {
        Dialogs.showInstruction(getActivity(), getString(R.string.alert_dialog_message_credit_instructions));
    }

}

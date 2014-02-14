package com.sevendesign.planitprom.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.TipDetailsAdapter;
import com.sevendesign.planitprom.models.TipCategory;
import com.sevendesign.planitprom.models.TipItem;

public class TipDetailsFragment extends Fragment {
    public static final String TAG = "TipDetailsFragment";
    public static final String TIP = "tip";

    private TextView tipTitleText;
    private ListView tipDetailsList;
    private TipCategory tipCategory;
    private TipDetailsAdapter tipDetailsAdapter;
    private ImageView headerBackground;

    public static TipDetailsFragment newInstance(TipCategory tip) {
        TipDetailsFragment fragment = new TipDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(TIP, tip);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tipCategory = getTip();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip_details, null);
        initActionBar();
        initViews(view);
        setContent();
        setList();
        return view;
    }

    private void initViews(View rootView) {
        tipTitleText = (TextView) rootView.findViewById(R.id.tip_details_title_text);
        tipDetailsList = (ListView) rootView.findViewById(R.id.tip_details_list);
        headerBackground = (ImageView) rootView.findViewById(R.id.tip_details_image);
        View listSpacer = View.inflate(getActivity(), R.layout.tip_details_list_footer, null);
        tipDetailsList.addHeaderView(listSpacer);
        tipDetailsList.addFooterView(listSpacer);
    }

    private void setContent() {
        tipTitleText.setText(tipCategory.getName().toUpperCase());
		headerBackground.setImageDrawable(getActivity().getResources().getDrawable(tipCategory.getBackground()));
    }

    private void setList() {
		List<TipItem> tipDetails = tipCategory.getTipItems();
        tipDetailsAdapter = new TipDetailsAdapter(getActivity(), tipDetails);
        tipDetailsList.setAdapter(tipDetailsAdapter);
    }

    private TipCategory getTip() {
        Bundle bundle = getArguments();
		TipCategory item = new TipCategory("");
        if (bundle != null && bundle.containsKey(TIP)) {
            item = bundle.getParcelable(TIP);
        }
        return item;
    }

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        actionBar.setCustomView(null);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}




















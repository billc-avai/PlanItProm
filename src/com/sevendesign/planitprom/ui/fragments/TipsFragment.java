package com.sevendesign.planitprom.ui.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.TipsAdapter;
import com.sevendesign.planitprom.database.manager.DbManager;
import com.sevendesign.planitprom.database.models.Budget;
import com.sevendesign.planitprom.models.TipCategory;
import com.sevendesign.planitprom.models.TipItem;

public class TipsFragment extends Fragment {
    public static final String TAG = "TipsFragment";
	private List<TipCategory> tips;
    private ListView tipsList;
    private TipsAdapter tipsAdapter;
	private String gender;
	

	public static final String MALE_TIPS_FILE = "tips_male.xml";
	public static final String FEMALE_TIPS_FILE = "tips_female.xml";
	public static final String UNISEX_TIPS_FILE = "tips_unisex.xml";
	public static final String TIPS_TAG = "TIPS";
	public static final String TIP_TAG = "TIP";
	public static final String CATEGORY_TAG = "CATEGORY";
	public static final String TITLE_TAG = "TITLE";
	public static final String CONTENT_TAG = "CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		List<Budget> budgets = new DbManager().getBudgets();
		if (budgets.size() > 0) {
			gender = budgets.get(0).getGender();
		} else {
			gender = null;
		}
		
		if (gender != null) {
			tips = getTipCategories(gender);
		} else {
			getActivity().finish();
		}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, null);
        initActionBar();
        initViews(view);
        fillList();
        return view;
    }

    private void initViews(View rootView) {
        tipsList = (ListView) rootView.findViewById(R.id.tips_list);
        View listSpacer = View.inflate(getActivity(), R.layout.tips_list_header, null);
        tipsList.addHeaderView(listSpacer);
        tipsList.addFooterView(listSpacer);
    }

    private void fillList() {
        tipsAdapter = new TipsAdapter(getActivity(), tips);
        tipsList.setAdapter(tipsAdapter);
    }

	private List<TipCategory> getTipCategories(String gender) {
		List<TipCategory> tips;

		if (gender == null || gender.length() == 0) {
			return new ArrayList<TipCategory>();
		}
		
		String genderTipsFile;
		int headerArrayId;
		
		if (gender.equalsIgnoreCase("male")) {
			genderTipsFile = MALE_TIPS_FILE;
			headerArrayId = R.array.tip_headers_male;
		} else {
			genderTipsFile = FEMALE_TIPS_FILE;
			headerArrayId = R.array.tip_headers_female;
		}
		
		tips = parseTips(genderTipsFile);
		
		if (tips == null) {
			tips = new ArrayList<TipCategory>();
		} else {
			//array of header backgrounds needs to have the same size and order as the list of tip categories for this to work correctly 
			String[] headerBackgrounds = getResources().getStringArray(headerArrayId);
			
			if (headerBackgrounds != null && tips.size() > 0) {
				
				for (int i = 0; i < tips.size(); i++) {
					String headerBkgd = null;
					
					try {
						headerBkgd = headerBackgrounds[i];
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
					
					if (headerBkgd != null) {
						tips.get(i).setBackground(getResources().getIdentifier(headerBkgd, "drawable", getActivity().getPackageName()));
					}
				}
			}

		}

		return tips;
	}
	
	private List<TipCategory> parseTips(String tipsFileName) {
		Map<String, TipCategory> tipCategories = new LinkedHashMap<String, TipCategory>();

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			InputStream is = getActivity().getAssets().open(tipsFileName);
			parser.setInput(new InputStreamReader(is));

			String category = null;
			TipItem tip=null;
			String text = null;

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				
				switch (eventType) {
				
				case XmlPullParser.START_TAG:
					if (tagName.equalsIgnoreCase(TIP_TAG)) {
						tip = new TipItem();
					}
					break;
				
				case XmlPullParser.TEXT:
					String textToken = parser.getText().trim();
					
					if (textToken.length() > 0) {
						text = parser.getText();
					}
					break;
				
				case XmlPullParser.END_TAG:
					if(tip!=null){
						if(tagName.equals(CATEGORY_TAG)){
							category = text;
						}else if(tagName.equals(TITLE_TAG)){
							tip.setTitle(text);
						}else if(tagName.equals(CONTENT_TAG)){
							tip.setContent(text);
						} else if (tagName.equals(TIP_TAG)) {
							TipCategory tipCategory = tipCategories.get(category);
							
							if (tipCategory == null) {
								tipCategory = new TipCategory(category);
								tipCategories.put(category, tipCategory);
							}
							
							tipCategory.addTipItem(tip);
						}
					}

					break;
				}
				eventType = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        // getting there tip
//        String gettingThereTitle = res.getString(R.string.tip_getting_there_title);
//        String[] gettingThereDetails = res.getStringArray(R.array.tip_getting_there_details);
//        TipItem gettingThereItem = new TipItem();
//        gettingThereItem.setTitle(gettingThereTitle);
//        gettingThereItem.setIndividualTips(new ArrayList<String>(Arrays.asList(gettingThereDetails)));
//        gettingThereItem.setBackground(R.drawable.tip_image_0);


        // add all categories
//        tips.add(gettingThereItem);

		return new ArrayList<TipCategory>(tipCategories.values());
    }

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("");
        actionBar.setCustomView(null);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}




















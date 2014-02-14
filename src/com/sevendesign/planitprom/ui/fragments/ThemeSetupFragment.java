package com.sevendesign.planitprom.ui.fragments;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sevendesign.planitprom.R;
import com.sevendesign.planitprom.adapters.ThemeAdapter;
import com.sevendesign.planitprom.models.ThemeItem;
import com.sevendesign.planitprom.ui.actions.ShowFragmentAction;
import com.sevendesign.planitprom.ui.actions.ThemeChangedAction;
import com.sevendesign.planitprom.utils.Utils;

import de.greenrobot.event.EventBus;

public class ThemeSetupFragment extends Fragment {
    public static final String TAG = "ThemeSetupFragment";
    public static final String ITEM_NAME = "item_name";
    private ListView themesListView = null;
    private ThemeAdapter themeAdapter = null;
    private boolean callFromSettings = false;


    public ThemeSetupFragment(boolean fromSettings) {
        this.callFromSettings = fromSettings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme_setup, null);

        initViews(view);
        initContent();

        return view;
    }

    private void initViews(View view) {
        themesListView = (ListView) view.findViewById(R.id.theme_setup_grid);
        View header = View.inflate(getActivity(), R.layout.theme_setup_header, null);
        themesListView.addHeaderView(header);
    }

    private View.OnClickListener themeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ThemeItem tag = (ThemeItem)v.getTag();
                if(tag!=null) {
                    ThemeChangedAction theme = ThemeChangedAction.SET_THEME;
                    theme.setName(tag.getThemeName());
                    EventBus.getDefault().post(theme);

                    if(callFromSettings) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        ShowFragmentAction action = ShowFragmentAction.SETTINGS;
                        action.setAddBackStack(false);
                        EventBus.getDefault().post(action);
                    }
                }
            } catch(Exception exc){}
        }
    };

    private void initContent() {
        ArrayList<ThemeItem> themeList = getThemeList();
        int cellWidth = getCellWidth();
        int cellHeight = cellWidth;
        themeAdapter = new ThemeAdapter(getActivity(), themeClickListener, themeList, cellWidth, cellHeight);
        themesListView.setAdapter(themeAdapter);
    }

    private void initActionBar() {
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(R.string.fragments_theme_setup_title);
        actionBar.setCustomView(null);
    }

    private int getCellWidth() {
        int displayWidth = Utils.getScreenWidth(getActivity());

        // get dimensions
        Resources res = getActivity().getResources();
        int layoutPaddingLeft = res.getDimensionPixelSize(R.dimen.theme_setup_list_row_padding_left);
        int layoutPaddingRight = res.getDimensionPixelSize(R.dimen.theme_setup_list_row_padding_right);
        int verticalDivider = res.getDimensionPixelSize(R.dimen.theme_setup_list_vertical_divider); // two dividers

        // calculate width and height for grid cell
        int cellNumber = 3; // update ThemeListItemModel before edit this field
        int cellWidth = ((displayWidth - (layoutPaddingLeft + layoutPaddingRight) - verticalDivider * (cellNumber - 1)) / cellNumber);
        return cellWidth;
    }

    public static final String THEME_DEFAULT = "theme1";
    private static ArrayList<ThemeItem> getThemeList() {
        ArrayList<ThemeItem> themeList = new ArrayList<ThemeItem>(16);

        themeList.add(new ThemeItem(THEME_DEFAULT, R.drawable.prev1, R.drawable.bg1));
//        themeList.add(new ThemeItem("theme2", R.drawable.prev2, R.drawable.bg2));
//        themeList.add(new ThemeItem("theme3", R.drawable.prev3, R.drawable.bg3));
//        themeList.add(new ThemeItem("theme4", R.drawable.prev4, R.drawable.bg4));
//        themeList.add(new ThemeItem("theme5", R.drawable.prev5, R.drawable.bg5));
//        themeList.add(new ThemeItem("theme6", R.drawable.prev6, R.drawable.bg6));
//        themeList.add(new ThemeItem("theme7", R.drawable.prev7, R.drawable.bg7));
//        themeList.add(new ThemeItem("theme8", R.drawable.prev8, R.drawable.bg8));
//        themeList.add(new ThemeItem("theme9", R.drawable.prev9, R.drawable.bg9));
//        themeList.add(new ThemeItem("theme10", R.drawable.prev10, R.drawable.bg10));
//        themeList.add(new ThemeItem("theme11", R.drawable.prev11, R.drawable.bg11));
//        themeList.add(new ThemeItem("theme12", R.drawable.prev12, R.drawable.bg12));
//        themeList.add(new ThemeItem("theme13", R.drawable.prev13, R.drawable.bg13));
//        themeList.add(new ThemeItem("theme14", R.drawable.prev14, R.drawable.bg14));
//        themeList.add(new ThemeItem("theme15", R.drawable.prev15, R.drawable.bg15));
//        themeList.add(new ThemeItem("theme16", R.drawable.prev16, R.drawable.bg16));

        return themeList;
    }

    public static ThemeItem getTheme(String name) {
        ThemeItem defTheme = new ThemeItem(THEME_DEFAULT, R.drawable.prev1, R.drawable.bg1);
        for(ThemeItem tempTheme : getThemeList()) {
            if(tempTheme.getThemeName().equalsIgnoreCase(name)) {
                return tempTheme;
            }
        }
        return defTheme;
    }
}













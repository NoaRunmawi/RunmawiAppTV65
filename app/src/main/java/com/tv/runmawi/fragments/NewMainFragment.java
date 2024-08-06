package com.tv.runmawi.fragments;


import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Row;

import com.tv.runmawi.R;

public class NewMainFragment extends BrowseFragment {
    private static final long HEADER_ID_1 = 1;
    private static final String HEADER_NAME_1 = "HOME";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "Movies";
    private static final long HEADER_ID_3 = 3;
    private static final String HEADER_NAME_3 = "TV Shows";
    private static final long HEADER_ID_4 = 4;
    private static final String HEADER_NAME_4 = "User";
    private BackgroundManager mBackgroundManager;

    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        loadData();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        getMainFragmentRegistry().registerFragment(PageRow.class,
                new PageRowFragmentFactory(mBackgroundManager));
    }

    private void setupUi() {
        //   setHeadersState(HEADERS_ENABLED);
        //    setHeadersTransitionOnBackEnabled(true);
        //    setBrandColor(getResources().getColor(R.color.fastlane_background));
        //   setTitle("Title goes here");

        //    setBadgeDrawable(getActivity().getResources().getDrawable(
        //      R.drawable.top_left));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.red));

        //  prepareEntranceTransition();
    }

    private void loadData() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);
        //    createRows();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                //       startEntranceTransition();
            }
        }, 2000);
    }

    private void createRows() {
        HeaderItem headerItem1 = new HeaderItem(HEADER_ID_1, HEADER_NAME_1);
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        HeaderItem headerItem2 = new HeaderItem(HEADER_ID_2, HEADER_NAME_2);
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

        HeaderItem headerItem3 = new HeaderItem(HEADER_ID_3, HEADER_NAME_3);
        PageRow pageRow3 = new PageRow(headerItem3);
        mRowsAdapter.add(pageRow3);

        HeaderItem headerItem4 = new HeaderItem(HEADER_ID_4, HEADER_NAME_4);
        PageRow pageRow4 = new PageRow(headerItem4);
        mRowsAdapter.add(pageRow4);
    }

    private class PageRowFragmentFactory extends FragmentFactory {
        private final BackgroundManager mBackgroundManager;

        PageRowFragmentFactory(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;
            mBackgroundManager.setDrawable(null);
            if (row.getHeaderItem().getId() == HEADER_ID_1) {
                return new HomeFragment();
            } else if (row.getHeaderItem().getId() == HEADER_ID_2) {
                return new MoviesFragment();
            } else if (row.getHeaderItem().getId() == HEADER_ID_3) {
                return new TVShowsFragment();
            } else if (row.getHeaderItem().getId() == HEADER_ID_4) {
                //    setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.top_left));
                return new UserSettingFragment();
            }

            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }


}

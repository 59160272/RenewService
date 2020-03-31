package com.example.renewseviceqq;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class TabsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private final ArrayList<TabInfo> mTabs = new ArrayList<>();
    private final FragmentActivity mActivity;

    public TabsPagerAdapter(@NonNull FragmentActivity activity, FragmentManager fragmentManager) {
        super(fragmentManager);
        mActivity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }
    public void addTab(Class<SearchPostFragment> clss, @Nullable Bundle args, String title) {
        mTabs.add(new TabInfo(title, Fragment.instantiate(mActivity, clss.getName(), args)));
    }
    public void addTab2(Class<SearchTechFragment> clss, @Nullable Bundle args, String title) {
        mTabs.add(new TabInfo(title, Fragment.instantiate(mActivity, clss.getName(), args)));
    }
    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }
    static final class TabInfo {
        String title;
        Fragment fragment;

        public TabInfo(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }
}

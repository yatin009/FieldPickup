package io.webguru.fieldpickup.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.webguru.fieldpickup.AndroidSlidingTab.SlidingTabLayout;
import io.webguru.fieldpickup.POJO.Tab;
import io.webguru.fieldpickup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    @Bind(R.id.viewpager_container)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    SlidingTabLayout mTabs;

    SectionsPagerAdapter mSectionsPagerAdapter;

    static ArrayList<Tab> tabs;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static Tab getTab(String title){
        Tab tab = null;
        for(Tab tab1 : tabs){
            if(tab1.getTitle().equals(title)){
                tab = tab1;
            }
        }
        return tab;
    }

    public static ArrayList<Tab> getAllTab(){
        return tabs;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        setupTabs();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);    //define any color in xml resources and set it here, I have used white
            }
        });
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabs.setViewPager(mViewPager);

        return view;
    }

    private void setupTabs(){
        tabs = new ArrayList<>();
        Tab tab1 = new Tab(new PendingDocketsFragments(), "Pending");
        Tab tab2 = new Tab(new DoneDocketsFragments(), "Done");
        tabs.add(tab1);
        tabs.add(tab2);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return tabs.get(position).getFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).getTitle();
        }


    }

}

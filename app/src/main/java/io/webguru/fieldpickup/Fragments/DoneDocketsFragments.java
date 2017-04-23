package io.webguru.fieldpickup.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TabHost;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.webguru.fieldpickup.Adapters.PendingDocketAdapter;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.Tab;
import io.webguru.fieldpickup.R;

public class DoneDocketsFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyler_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_dialog)
    ProgressBar mProgressDialog;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    //RecyclerView objects
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    ArrayList<Docket> dockets;
    public DoneDocketsFragments() {
        // Required empty public constructor
    }

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done_dockets_fragments, container, false);
        ButterKnife.bind(this, view);
        this.context = view.getContext();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.circlePVRim),

                getResources().getColor(R.color.circlePVBar),

                getResources().getColor(R.color.appBarScrim),

                getResources().getColor(R.color.orange));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDocketList(false);
            }
        }, 2000);
    }

    private void getDocketList(final boolean isRefresh){
        boolean isPending = false;
        dockets = GlobalFunction.getDocketList(isPending, context);
        loadRecyclerViewElements();
        if (isRefresh) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadRecyclerViewElements(){
        mProgressDialog.setVisibility(View.GONE);
//        // use a linear layout manager
        mAdapter = new PendingDocketAdapter(dockets, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDocketList(true);
            }
        }, 2000);

    }

    @Override
    public void onResume() {
        super.onResume();
        onStart();
    }
}

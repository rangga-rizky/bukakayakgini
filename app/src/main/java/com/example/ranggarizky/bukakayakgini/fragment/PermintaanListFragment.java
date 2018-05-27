package com.example.ranggarizky.bukakayakgini.fragment;



import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.PermintaanListActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.adapter.RequestAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.RequestSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.RequestVerticalSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.TawaranRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.GridSpacingItemDecoration;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermintaanListFragment extends Fragment {
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ArrayList<RequestObject> requests;
    private RequestVerticalSquareRecyclerAdapter mAdapter;


    public PermintaanListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_permintaan_list, container, false);
        ButterKnife.bind(this,view);

        setHasOptionsMenu(true);
        requests = new ArrayList<>();
         mAdapter = new RequestVerticalSquareRecyclerAdapter(getActivity(),requests);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                loadData();
            }
        });
        loadData();
        return view;
    }


    private void loadData(){

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.getAllPermintaan("ant0k","1","1","50");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() >0) {
                        requests.clear();
                        requests.addAll(apiresponse.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponsePermintaan> call, Throwable t) {
                // Log error
                progressBar.setVisibility(View.GONE);
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menawarkan_barang_menu, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        super.onCreateOptionsMenu(menu,inflater);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                if(s.length() > 0){
                    loadRequestbyName(s);
                }else{
                    loadData();
                }
                return false;
            }
        });
    }

    private void loadRequestbyName(String name){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.searchDemand(name,"ant0k","1","10");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() >0) {
                        requests.clear();
                        requests.addAll(apiresponse.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponsePermintaan> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

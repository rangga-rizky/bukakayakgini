package com.example.ranggarizky.bukakayakgini.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.MainActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.adapter.RequestVerticalSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.TawaranKuRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.model.ResponseTawaran;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.GridSpacingItemDecoration;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
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
public class TawarankuFragment extends Fragment {
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    LinearLayout empty_view;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    int toolbarMarginOffset = 0;
    private  ArrayList<RequestObject> datas;
    private  TawaranKuRecyclerAdapter mAdapter;
    private SessionManager sessionManager;


    public TawarankuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tawaranku, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        datas = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        mAdapter = new TawaranKuRecyclerAdapter(getActivity(),datas);

        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(mAdapter);
        loadData();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                loadData();
            }
        });


        return view;
    }


    private int dp(int inPixels){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inPixels, getResources().getDisplayMetrics());
    }

    private void loadData(){

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.fetchmySupplies(sessionManager.getUid(),"ant0k");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                progressBar.setVisibility(View.GONE);
                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() >0) {

                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        datas.clear();
                        datas.addAll(apiresponse.getData());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();


    }
}

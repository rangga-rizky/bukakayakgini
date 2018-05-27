package com.example.ranggarizky.bukakayakgini.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.BeforeRequestActivity;
import com.example.ranggarizky.bukakayakgini.NotificationActivity;
import com.example.ranggarizky.bukakayakgini.PermintaanListActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.adapter.RequestInterestSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.RequestTrendingSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseNotif;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.Validation;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    @BindView(R.id.horizontal_request_trend_recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.layoutInterest)
    RelativeLayout layoutInterest;
    @BindView(R.id.progressBarInterest)
    ProgressBar progressBarInterest;
    @BindView(R.id.progressBarTrending)
    ProgressBar progressBarTrending;
    @BindView(R.id.horizontal_request_interest_recycler_view)
    RecyclerView horizontal_request_interest_recycler_view;
    @BindView(R.id.editBarang)
    EditText editBarang;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    private RequestTrendingSquareRecyclerAdapter mTrendingadapter;
    private RequestInterestSquareRecyclerAdapter mInterestadapter;
    private ArrayList<RequestObject> requestTrandings;
    private ArrayList<RequestObject> requestInterest;
    private TextView txtCountNotif;
    private BroadcastReceiver receiver;
    private SessionManager sessionManager;


    public BrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        sessionManager = new SessionManager(getActivity());
        requestTrandings = new ArrayList<>();
        requestInterest = new ArrayList<>();


        LinearLayoutManager horizontalLayoutManagaerSquare
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view.setLayoutManager(horizontalLayoutManagaerSquare);
        mTrendingadapter =  new RequestTrendingSquareRecyclerAdapter(getActivity(),requestTrandings);
        recycler_view.setAdapter(mTrendingadapter);

        LinearLayoutManager horizontalLayoutManagaerSquareInterest
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_request_interest_recycler_view.setLayoutManager(horizontalLayoutManagaerSquareInterest);
        mInterestadapter =  new RequestInterestSquareRecyclerAdapter(getActivity(),requestInterest);
        horizontal_request_interest_recycler_view.setAdapter(mInterestadapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                loadTrending();
                loadInterest();
            }
        });

        loadTrending();
        loadInterest();
        return view;
    }

    @OnClick(R.id.btnRequest)
    public void request(View view){
        if(Validation.checkEmpty(editBarang)){
            Intent intent = new Intent(getActivity(), BeforeRequestActivity.class);
            intent.putExtra("nama",editBarang.getText().toString());
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnLihatSemua)
    public void lihatRequest(View view){
        startActivity(new Intent(getActivity(), PermintaanListActivity.class));
    }

    @OnClick(R.id.btnLihatSemuaInterest)
    public void lihatSemuaInterest(View view){
       startActivity(new Intent(getActivity(), PermintaanListActivity.class));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.browse_menu, menu);

        final View menu_notif = menu.findItem(R.id.action_notif).getActionView();
        menu_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCountNotif.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });
        txtCountNotif = (TextView) menu_notif.findViewById(R.id.txtJumlah);
        txtCountNotif.setVisibility(View.GONE);
        loadNotif();


    }

    private void loadNotif(){
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseNotif> call = apiService.countNotReadedNotif(sessionManager.getUid(),"0","ant0k");

        //proses call
        call.enqueue(new Callback<ResponseNotif>() {
            @Override
            public void onResponse(Call<ResponseNotif> call, Response<ResponseNotif> response) {

                if(txtCountNotif!=null){
                    if(response.body().getCount() > 0){
                        updateNotifCOunt(response.body().getCount());
                        txtCountNotif.setVisibility(View.VISIBLE);
                    }else{

                        txtCountNotif.setVisibility(View.GONE);
                    }

                }
            }


            @Override
            public void onFailure(Call<ResponseNotif> call, Throwable t) {
                Log.e("notif", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

    public void updateNotifCOunt(final int jumlahNotif) {
        if (txtCountNotif == null) return;
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (jumlahNotif == 0)
                        txtCountNotif.setVisibility(View.INVISIBLE);
                    else {
                        txtCountNotif.setVisibility(View.VISIBLE);
                        txtCountNotif.setText(Integer.toString(jumlahNotif));
                    }
                }
            });
        }

    }



    private void loadInterest(){

        progressBarInterest.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.fetchInterestRequest(sessionManager.getUid(),"ant0k","1","10");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {
                progressBarInterest.setVisibility(View.GONE);
                horizontal_request_interest_recycler_view.setVisibility(View.VISIBLE);
                List<RequestObject> apiresponse = response.body().getData();
                if(apiresponse.size() > 0){
                    requestInterest.clear();
                    requestInterest.addAll(apiresponse);
                    mInterestadapter.notifyDataSetChanged();
                    layoutInterest.setVisibility(View.VISIBLE);
                }else{
                    layoutInterest.setVisibility(View.GONE);
                    horizontal_request_interest_recycler_view.setVisibility(View.GONE);
                }
            }


            @Override
            public void onFailure(Call<ResponsePermintaan> call, Throwable t) {
               // Log.e("notif", "onFailure: ", t.fillInStackTrace());
                progressBarInterest.setVisibility(View.GONE);
            }
        });
    }

    private void loadTrending(){
        progressBarTrending.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.fetchTrending("ant0k","10");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {
                recycler_view.setVisibility(View.VISIBLE);
                progressBarTrending.setVisibility(View.GONE);
                List<RequestObject> apiresponse = response.body().getData();
                if(apiresponse.size() > 0){
                    requestTrandings.clear();
                    requestTrandings.addAll(apiresponse);
                    mTrendingadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponsePermintaan> call, Throwable t) {
                // Log.e("notif", "onFailure: ", t.fillInStackTrace());
                progressBarTrending.setVisibility(View.GONE);
            }
        });
    }



    public void onResume(){
        super.onResume();

        IntentFilter filter = new IntentFilter("com.ranggarizky.bukaKayakGini");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(getActivity() != null){
                   loadNotif();
                }
            }
        };
        getActivity().registerReceiver(receiver, filter);

        //  AppBarLayout.LayoutParams params =
        //        (AppBarLayout.LayoutParams) ((MainActivity) getActivity()).getToolbar().getLayoutParams();
        // params.setScrollFlags(0);
        loadNotif();

    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }


}

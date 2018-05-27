package com.example.ranggarizky.bukakayakgini.fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.MainActivity;
import com.example.ranggarizky.bukakayakgini.NotificationActivity;
import com.example.ranggarizky.bukakayakgini.PrepareRequestActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.adapter.RequestAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.RequestSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseNotif;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.BadgeView;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestkuFragment extends Fragment {
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.empty_view)
    LinearLayout empty_view;
    private SessionManager sessionManager;
    private ArrayList<RequestObject> requests;
    private  RequestAdapter mAdapter;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private BroadcastReceiver receiver;
    private  TextView txtCountNotif;


    public RequestkuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requestku, container, false);;
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        sessionManager = new SessionManager(getActivity());
        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        requests = new ArrayList<>();
        mAdapter = new RequestAdapter(getActivity(),requests);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    fab.hide();
                } else{
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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

    @OnClick(R.id.btnRequest)
    public void request(View view){
        startActivity(new Intent(getActivity(), PrepareRequestActivity.class));
    }

    @OnClick(R.id.fab)
    public void buatRequest(View view){
        startActivity(new Intent(getActivity(), PrepareRequestActivity.class));
    }

    private void loadData(){

        swipe.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.getPermintaanku(sessionManager.getUid(),"ant0k","1","20");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                progressBar.setVisibility(View.GONE);
                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() >0){
                        requests.clear();
                        requests.addAll(apiresponse.getData());
                        mAdapter.notifyDataSetChanged();
                        empty_view.setVisibility(View.GONE);
                        swipe.setVisibility(View.VISIBLE);
                    }else{
                        empty_view.setVisibility(View.VISIBLE);
                        swipe.setVisibility(View.GONE);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.requestku_menu, menu);
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

    private void loadRequestbyName(String name){
        swipe.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.searchDemand(name,"ant0k","1","10",sessionManager.getUid());

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                progressBar.setVisibility(View.GONE);
                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() >0){
                        requests.clear();
                        requests.addAll(apiresponse.getData());
                        mAdapter.notifyDataSetChanged();
                        empty_view.setVisibility(View.GONE);
                        swipe.setVisibility(View.VISIBLE);
                    }else{
                        empty_view.setVisibility(View.VISIBLE);
                        swipe.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_notif : startActivity(new Intent(getActivity(), NotificationActivity.class));break;
            case R.id.action_filter : CharSequence colors[] = new CharSequence[] {"Sedang Ditawari", "Menunggu Tawaran","Transaksi","Request Ditutup","Lihat Semua"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Filter Berdasarkan");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:loadByStatus("3");break;
                            case 1:loadByStatus("1");break;
                            case 2:loadByStatus("2");break;
                            case 3:loadByStatus("0");break;
                            case 4:loadData();break;
                        }
                    }
                });
                builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadByStatus(String status){

        swipe.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.getPermintaanku(status,sessionManager.getUid(),"ant0k","1","10");

        //proses call
        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                progressBar.setVisibility(View.GONE);
                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok",String.valueOf(response.code()));
                } else {
                    if(apiresponse.getData().size() >0){
                        requests.clear();
                        requests.addAll(apiresponse.getData());
                        mAdapter.notifyDataSetChanged();
                        empty_view.setVisibility(View.GONE);
                        swipe.setVisibility(View.VISIBLE);
                    }else{
                        empty_view.setVisibility(View.VISIBLE);
                        swipe.setVisibility(View.GONE);
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


    public void onResume(){
        super.onResume();

        IntentFilter filter = new IntentFilter("com.ranggarizky.bukaKayakGini");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(getActivity() != null){
                    loadData();
                    loadNotif();
                }
            }
        };
        getActivity().registerReceiver(receiver, filter);
        loadData();

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

package com.example.ranggarizky.bukakayakgini;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.RequestJoinAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.RequestVerticalSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.Join;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseJoin;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.util.GridSpacingItemDecoration;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.Validation;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestJoinActivity extends AppCompatActivity {
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SessionManager sessionManager;
    private ArrayList<Join> headers;
    private RequestJoinAdapter mAdapter;
    private ArrayList<Join> items;
    private String id;
    private int jumlahRequest=0,jumlahUdahJoin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_join);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = getIntent().getStringExtra("id");
        Log.d("gurara",id);
        headers = new ArrayList<>();
        items = new ArrayList<>();
        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadWhoAlreadyJoin();
    }

    private void loadWhoAlreadyJoin(){

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseJoin> call = apiService.getAllUserJoin(id,"1","ant0k");

        //proses call
        call.enqueue(new Callback<ResponseJoin>() {
            @Override
            public void onResponse(Call<ResponseJoin> call, Response<ResponseJoin> response) {

                ResponseJoin apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                    progressBar.setVisibility(View.GONE);
                } else {
                    loadWhoRequest();
                    if(apiresponse.getData().size() >0) {
                        items.clear();
                        items.addAll(apiresponse.getData());
                        jumlahUdahJoin = items.size();
                        loadAdapter();
                        Log.e("gurara",String.valueOf(items.size()));
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }

                }

            }


            @Override
            public void onFailure(Call<ResponseJoin> call, Throwable t) {
                // Log error
                progressBar.setVisibility(View.GONE);
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWhoRequest(){

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseJoin> call = apiService.getAllUserJoin(id,"0","ant0k");

        //proses call
        call.enqueue(new Callback<ResponseJoin>() {
            @Override
            public void onResponse(Call<ResponseJoin> call, Response<ResponseJoin> response) {

                ResponseJoin apiresponse = response.body();

                progressBar.setVisibility(View.GONE);
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {

                    recyclerView.setVisibility(View.VISIBLE);
                    if(apiresponse.getData().size() >0) {
                        headers.clear();
                        headers.addAll(apiresponse.getData());
                        jumlahRequest = headers.size();
                        Log.e("gurara",String.valueOf(headers.size()));
                        loadAdapter();
                    }else{
                        headers.add(new Join());
                        jumlahRequest = 0;
                        loadAdapter();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseJoin> call, Throwable t) {
                // Log error
                progressBar.setVisibility(View.GONE);
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void loadAdapter(){
        mAdapter = new RequestJoinAdapter(this,items,headers,String.valueOf(jumlahRequest),String.valueOf(jumlahUdahJoin));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void load(){
        loadWhoAlreadyJoin();
    }




}

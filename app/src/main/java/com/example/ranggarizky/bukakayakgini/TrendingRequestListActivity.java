package com.example.ranggarizky.bukakayakgini;

import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.RequestVerticalSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.util.GridSpacingItemDecoration;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingRequestListActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String id_induk;

    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ArrayList<RequestObject> requests;
    private RequestVerticalSquareRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_request_list);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id_induk = getIntent().getStringExtra("id_induk");

        requests = new ArrayList<>();
        mAdapter = new RequestVerticalSquareRecyclerAdapter(this,requests);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
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

    }

    private void loadData(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.fetchChild(id_induk,"ant0k","1","50");

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
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
}
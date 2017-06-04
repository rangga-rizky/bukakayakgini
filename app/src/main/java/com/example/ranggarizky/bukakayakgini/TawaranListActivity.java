package com.example.ranggarizky.bukakayakgini;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.RequestAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.TawaranRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.TawaranRekomendasiAdapter;
import com.example.ranggarizky.bukakayakgini.model.ResponseTawaran;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.GridSpacingItemDecoration;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TawaranListActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private String id_request;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private TawaranRekomendasiAdapter mAdapter;

    @BindView(R.id.empty_view)
    RelativeLayout empty_view;
    private static boolean tandai = false,baru = false,bekas = false,lokasiAktif = false;
    private String idKota,idProvinsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tawaran_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id_request = getIntent().getStringExtra("id");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.GONE);
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
        Call<ResponseTawaran> call = apiService.fetchSupplyByDemand(id_request,"ant0k","20");

        //proses call
        call.enqueue(new Callback<ResponseTawaran>() {
            @Override
            public void onResponse(Call<ResponseTawaran> call, Response<ResponseTawaran> response) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ResponseTawaran apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("tawaranlist", "no response");
                } else {
                    if(apiresponse.getData().size() >0) {

                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        setAdapter(response.body().getData(),false);
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseTawaran> call, Throwable t) {
                // Log error
                progressBar.setVisibility(View.GONE);
                Log.e("tawaranlist", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataSortBy(String sortBy){

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseTawaran> call = apiService.fetchSupplyByDemand(id_request,"ant0k","20",sortBy);

        //proses call
        call.enqueue(new Callback<ResponseTawaran>() {
            @Override
            public void onResponse(Call<ResponseTawaran> call, Response<ResponseTawaran> response) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ResponseTawaran apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() >0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        setAdapter(response.body().getData(),true);
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);

                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseTawaran> call, Throwable t) {
                // Log error
                progressBar.setVisibility(View.GONE);
                Log.e("tawaranlist", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void setAdapter(ArrayList<Tawaran> data,Boolean isFiltered){
        //TawaranRecyclerAdapter mAdapter = new TawaranRecyclerAdapter(this,data,isFiltered);
        //recyclerView.setAdapter(mAdapter);
        if(isFiltered){
            mAdapter = new TawaranRekomendasiAdapter(this,data,null);
            recyclerView.setAdapter(mAdapter);
        }else{
            if(data.size() >= 5){
                Tawaran header = data.get(0);
                data.remove(0);
                mAdapter = new TawaranRekomendasiAdapter(this,data,header);
                recyclerView.setAdapter(mAdapter);

            }else{
                mAdapter = new TawaranRekomendasiAdapter(this,data,null);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    private void loadDatawithFilter(String isTandai,String statusKondisi,String kota){

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseTawaran> call = apiService.fetchSupplyByFilter(id_request,"ant0k","20",kota,statusKondisi,isTandai);

        //proses call
        call.enqueue(new Callback<ResponseTawaran>() {
            @Override
            public void onResponse(Call<ResponseTawaran> call, Response<ResponseTawaran> response) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ResponseTawaran apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("tawaranlist", "no response");
                } else {
                    if(apiresponse.getData().size() >0) {

                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        setAdapter(response.body().getData(),true);
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);

                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseTawaran> call, Throwable t) {
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

    @OnClick(R.id.btnFilter)
    public void filter(View v){
        Intent intent = new Intent(this,FilterTawaranActivity.class);
        intent.putExtra("lokasiAktif",lokasiAktif);
        intent.putExtra("tandai",tandai);
        intent.putExtra("baru",baru);
        intent.putExtra("bekas",bekas);
        intent.putExtra("idProvinsi",idProvinsi);
        intent.putExtra("idKota",idKota);
        startActivityForResult(intent,1);
    }

    @OnClick(R.id.btnUrutkan)
    public void urutkan(View v){
        CharSequence colors[] = new CharSequence[] {"Terbaru", "Termurah", "Termahal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Urutkan Berdasarkan");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:loadDataSortBy("terbaru");break;
                    case 1:loadDataSortBy("termurah");break;
                    case 2:loadDataSortBy("termahal");break;
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            String isTandai="",statusKondisi="",kota="";
            idKota = data.getStringExtra("idKota");
            idProvinsi = data.getStringExtra("idProvinsi");
            kota = data.getStringExtra("kota");
            tandai = data.getBooleanExtra("tertandai_checked",false);
            baru = data.getBooleanExtra("baru_checked",false);
            bekas = data.getBooleanExtra("bekas_checked",false);
            lokasiAktif = data.getBooleanExtra("lokasiAktif",false);
            if(data.getBooleanExtra("baru_checked",false) && data.getBooleanExtra("bekas_checked",false)){
                statusKondisi = "";
            }else if(data.getBooleanExtra("baru_checked",false)){
                statusKondisi = "1";
            }else if(data.getBooleanExtra("bekas_checked",false)){
                statusKondisi = "0";
            }

            if(data.getBooleanExtra("tertandai_checked",false)){
                isTandai = "3";
            }
            //Log.e("jalan",isTandai+" "+statusKondisi+" "+kota);
            loadDatawithFilter(isTandai,statusKondisi,kota);

        }
    }

}

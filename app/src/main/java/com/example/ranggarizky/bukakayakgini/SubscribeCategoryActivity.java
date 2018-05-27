package com.example.ranggarizky.bukakayakgini;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.CategorySubscribeRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.NotificationRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.Kategori;
import com.example.ranggarizky.bukakayakgini.model.Notif;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseKategori;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeCategoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtKategori)
    TextView txtKategori;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private String id_kategori;
    private CategorySubscribeRecyclerAdapter mAdapter;
    private ArrayList<Kategori> dataList;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_category);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sessionManager = new SessionManager(this);

        dataList = new ArrayList<>();
        mAdapter = new CategorySubscribeRecyclerAdapter(this,dataList);
        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        loadData();
    }

    @OnClick(R.id.txtKategori)
    public void txtKategori(View view){
        startActivityForResult(new Intent(this,CategoryActivity.class),1);
    }


    @OnClick(R.id.btnTambah)
    public void tambah(View view){
        if(id_kategori!=null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            WEB_API apiService = WEB_API.client.create(WEB_API.class);
            Call<ResponseApi> call = apiService.subscribe(sessionManager.getUid(),id_kategori,"ant0k");

            //proses call
            call.enqueue(new Callback<ResponseApi>() {
                @Override
                public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                    ResponseApi apiresponse = response.body();
                    progressDialog.dismiss();
                    if (response.body() == null) {
                        Log.e("cok", "no response");
                    } else {
                        if(apiresponse.getSuccess().equals("1")){
                            Toast.makeText(getApplicationContext(), "Kategori Berhasil disubscribe", Toast.LENGTH_SHORT).show();
                            loadData();
                        }else{
                            Toast.makeText(getApplicationContext(), "Terjadi kesalahan, coba lain kali", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


                @Override
                public void onFailure(Call<ResponseApi> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("cok", "onFailure: ", t.fillInStackTrace());
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "Harap pilih kategori untuk di subscribe", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadData(){
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseKategori> call = apiService.getSubscribebySeller(sessionManager.getUid(),"ant0k");

        //proses call
        call.enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {

                ResponseKategori apiresponse = response.body();
                dataList.clear();
                dataList.addAll(apiresponse.getData());
                mAdapter.notifyDataSetChanged();


            }


            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            txtKategori.setText(data.getStringExtra("nama_kategori"));
            id_kategori = data.getStringExtra("id_kategori");
        }
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

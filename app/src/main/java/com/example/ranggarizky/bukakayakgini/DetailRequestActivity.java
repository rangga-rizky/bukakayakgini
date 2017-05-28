package com.example.ranggarizky.bukakayakgini;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.BarangTawaranRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.model.ResponseTawaran;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRequestActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.imgBarang)
    ImageView imgBarang;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.txtDeskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txtKondisi)
    TextView txtKondisi;
    @BindView(R.id.txtbudget)
    TextView txtbudget;
    @BindView(R.id.txtTawaran)
    TextView txtTawaran;
    @BindView(R.id.txtKategori)
    TextView txtKategori;
    @BindView(R.id.txtUserJoin)
    TextView txtUserJoin;
    @BindView(R.id.btnTawar2)
    Button btnTawar2;
    @BindView(R.id.texttawarkan)
    TextView texttawarkan;
    String id_user;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private String id;
    private int jumlahTawaranku = 0;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);
        sessionManager =  new SessionManager(this);
        ButterKnife.bind(this);
        btnTawar2.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id = getIntent().getStringExtra("id");

        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        texttawarkan.setVisibility(View.GONE);

        loadData();
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

    private void loadData(){

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaanSingle> call = apiService.getPermintaanbyID(id,"ant0k","1");

        //proses call
        call.enqueue(new Callback<ResponsePermintaanSingle>() {
            @Override
            public void onResponse(Call<ResponsePermintaanSingle> call, Response<ResponsePermintaanSingle> response) {

                ResponsePermintaanSingle apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    txtTitle.setText(apiresponse.getData().getNama());
                    txtTawaran.setText(String.valueOf(apiresponse.getData().getSupplies().size()));
                    txtKategori.setText(apiresponse.getData().getKategori_detail().getNama());
                    if(apiresponse.getData().getKondisi().equals("0")){
                        txtKondisi.setText("Bekas");
                    }else if(apiresponse.getData().getKondisi().equals("1")){
                        txtKondisi.setText("Baru");
                    }
                    else{
                        txtKondisi.setText("Baru / Bekas");
                    }
                    id = apiresponse.getData().getId();
                    txtStatus.setText(apiresponse.getData().getStatus_caption());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    txtbudget.setText("Rp"+rupiahFormat.format(Double.parseDouble(apiresponse.getData().getHarga())));
                    txtDeskripsi.setText(apiresponse.getData().getDeskripsi());
                    txtUserJoin.setText(apiresponse.getData().getJumlah_join());
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getFoto())
                            .placeholder(R.drawable.dummy)
                            .into(imgBarang);
                    id_user = apiresponse.getData().getId_user();

                    loadTawaranku();
                }

            }


            @Override
            public void onFailure(Call<ResponsePermintaanSingle> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTawaranku(){

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseTawaran> call = apiService.fetchmySuppliesbyDemand(sessionManager.getUid(),"ant0k",id,"3");

        //proses call
        call.enqueue(new Callback<ResponseTawaran>() {
            @Override
            public void onResponse(Call<ResponseTawaran> call, Response<ResponseTawaran> response) {

                ResponseTawaran apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                    loadTawaranku();
                } else {
                    jumlahTawaranku = apiresponse.getData().size();
                    if(apiresponse.getData().size() >0) {

                        texttawarkan.setVisibility(View.VISIBLE);
                        setBarangTawaranadapter(response.body().getData());
                    }
                    if(jumlahTawaranku < 3){
                        if(!id_user.equals(sessionManager.getUid())){
                            btnTawar2.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseTawaran> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

    private  void setBarangTawaranadapter(ArrayList<Tawaran> data){

        BarangTawaranRecyclerAdapter mAdapter = new BarangTawaranRecyclerAdapter(this,data);
        recyclerView.setAdapter(mAdapter);
    }


    @OnClick(R.id.btnTawar2)
    public void tawar2(View view){
        Intent intent = new Intent(this,TawarkanActivity.class);
        intent.putExtra("id_permintaan",id);
        intent.putExtra("jumlahTawaranku",jumlahTawaranku);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTawaranku();
    }
}

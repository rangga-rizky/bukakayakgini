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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.BarangTawaranKuRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.BarangTawaranRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
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
    @BindView(R.id.imgmenunggu)
    ImageView menunggu;
    @BindView(R.id.imgditawari)
    ImageView ditawari;
    @BindView(R.id.imgtransaksi)
    ImageView transaksi;
    @BindView(R.id.imgselesai)
    ImageView selesai;
    @BindView(R.id.txtJumlahTawaran)
    TextView txtJumlahTawaran;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtDeskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txtKondisi)
    TextView txtKondisi;
    @BindView(R.id.txtbudget)
    TextView txtbudget;
    @BindView(R.id.txtKategori)
    TextView txtKategori;
    @BindView(R.id.txtMenunggu)
    TextView txtMenunggu;
    @BindView(R.id.txtSelesai)
    TextView txtSelesai;
    @BindView(R.id.txtTransksi)
    TextView txtTransksi;
    @BindView(R.id.txtDitawari)
    TextView txtDitawari;
    @BindView(R.id.txtNama)
    TextView txtNama;
    @BindView(R.id.txtJumlah)
    TextView txtJumlah;
    @BindView(R.id.txtKota)
    TextView txtKota;
    @BindView(R.id.txtbarangku)
    TextView txtbarangku;
    @BindView(R.id.imgAva)
    ImageView imgAva;
    @BindView(R.id.btnTawar)
    Button btnTawar;
    @BindView(R.id.footer)
    LinearLayout footer;
    String id_user;
    @BindView(R.id.barangku)
    LinearLayout barangku;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private String id,status_tawaran,induk_id;
    private int jumlahTawaranku = 0;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);
        sessionManager =  new SessionManager(this);
        ButterKnife.bind(this);
        footer.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id = getIntent().getStringExtra("id");

        barangku.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);

        loadData();
        cekTrans();
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
                    if(apiresponse.getData().getInduk()!=null){
                        induk_id = apiresponse.getData().getInduk().getId();
                    }
                    txtTitle.setText(apiresponse.getData().getNama());
                    txtJumlah.setText(apiresponse.getData().getJumlah());
                    txtJumlahTawaran.setText(String.valueOf(apiresponse.getData().getSupplies().size()));
                    txtKategori.setText(apiresponse.getData().getKategori_detail().getNama());
                    if(apiresponse.getData().getKondisi().equals("0")){
                        txtKondisi.setText("Bekas");
                    }else if(apiresponse.getData().getKondisi().equals("1")){
                        txtKondisi.setText("Baru");
                    }
                    else{
                        txtKondisi.setText("Baru / Bekas");
                    }
                    footer.setVisibility(View.VISIBLE);
                    id = apiresponse.getData().getId();
                    status_tawaran = apiresponse.getData().getStatus();
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    txtbudget.setText("Rp"+rupiahFormat.format(Double.parseDouble(apiresponse.getData().getHarga())));
                    txtDeskripsi.setText(apiresponse.getData().getDeskripsi());
                    txtNama.setText(apiresponse.getData().getUser().getUsername());
                    txtKota.setText(apiresponse.getData().getUser().getAddress().getCity());
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getFoto())
                            .placeholder(R.drawable.dummy)
                            .into(imgBarang);
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getUser().getAvatar())
                            .placeholder(R.drawable.dummy)
                            .into(imgAva);
                    id_user = apiresponse.getData().getId_user();


                    if(apiresponse.getData().getStatus().equals("0")){
                        txtSelesai.setTextColor(getResources().getColor(R.color.white));
                        txtSelesai.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_selesai));
                        selesai.setImageDrawable(getResources().getDrawable(R.drawable.circle_selesai));
                    }else if((apiresponse.getData().getStatus().equals("2"))){
                        txtTransksi.setTextColor(getResources().getColor(R.color.white));
                        txtTransksi.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rectangle_accent));
                        transaksi.setImageDrawable(getResources().getDrawable(R.drawable.circle_accent));
                        selesai.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));

                    }else{
                        if(apiresponse.getData().getSupplies().size() > 0){
                            txtDitawari.setTextColor(getResources().getColor(R.color.white));
                            txtDitawari.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_ditawari));
                            ditawari.setImageDrawable(getResources().getDrawable(R.drawable.circle_ditawari));
                            transaksi.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));
                            selesai.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));
                        }
                        else{
                            txtMenunggu.setTextColor(getResources().getColor(R.color.white));
                            txtMenunggu.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_menunggu));
                            menunggu.setImageDrawable(getResources().getDrawable(R.drawable.circle_menunggu));
                            transaksi.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));
                            selesai.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));
                            ditawari.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));
                        }
                    }

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
                        barangku.setVisibility(View.VISIBLE);
                        txtbarangku.setVisibility(View.VISIBLE);
                        setBarangTawaranadapter(response.body().getData());
                    }
                    if(jumlahTawaranku < 3){
                        if(!id_user.equals(sessionManager.getUid())){
                            if(!status_tawaran.equals("0")){
                                btnTawar.setVisibility(View.VISIBLE);
                            }
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

        BarangTawaranKuRecyclerAdapter mAdapter = new BarangTawaranKuRecyclerAdapter(this,data);
        recyclerView.setAdapter(mAdapter);
    }


    @OnClick(R.id.btnTawar)
    public void tawar2(View view){
        Intent intent = new Intent(this,TawarkanActivity.class);
        intent.putExtra("id_permintaan",id);
        intent.putExtra("jumlahTawaranku",jumlahTawaranku);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @OnClick(R.id.btnCopy)
    public  void join(View view){
        Intent intent = new Intent(this,InputCopyActivity.class);
        intent.putExtra("id",id);
        if(induk_id!=null){
            intent.putExtra("induk_id",induk_id);
        }else{
            intent.putExtra("induk_id",id);
        }
        startActivity(intent);
    }

    private  void cekTrans(){
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseApi> call = apiService.checkTransInBL(sessionManager.getUid(),sessionManager.getToken(),"ant0k");

        //proses call
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

            }


            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

}

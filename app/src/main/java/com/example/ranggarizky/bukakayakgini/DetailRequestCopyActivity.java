package com.example.ranggarizky.bukakayakgini;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRequestCopyActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.imgBarang)
    ImageView imgBarang;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.btnJoin)
    Button btnJoin;
    @BindView(R.id.txtNama)
    TextView txtNama;
    @BindView(R.id.txtKota)
    TextView txtKota;
    @BindView(R.id.txtDeskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txtKondisi)
    TextView txtKondisi;
    @BindView(R.id.txtbudget)
    TextView txtbudget;
    @BindView(R.id.txtKategori)
    TextView txtKategori;
    @BindView(R.id.txtJumlah)
    TextView txtJumlah;
    @BindView(R.id.imgAva)
    ImageView imgAva;
    @BindView(R.id.footer)
    LinearLayout footer;
    private String id,induk_id;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_copy);
        ButterKnife.bind(this);
        sessionManager =  new SessionManager(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id = getIntent().getStringExtra("id");
        cekTrans();
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
        Call<ResponsePermintaanSingle> call = apiService.getPermintaanbyID(id,sessionManager.getUid(),"ant0k","1");

        //proses call
        call.enqueue(new Callback<ResponsePermintaanSingle>() {
            @Override
            public void onResponse(Call<ResponsePermintaanSingle> call, Response<ResponsePermintaanSingle> response) {

                ResponsePermintaanSingle apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    txtTitle.setText(apiresponse.getData().getNama());
                    txtDate.setText(apiresponse.getData().getTanggal());
                    txtJumlah.setText(apiresponse.getData().getJumlah());
                    txtKategori.setText(apiresponse.getData().getKategori_detail().getNama());
                    if(apiresponse.getData().getKondisi().equals("0")){
                        txtKondisi.setText("Bekas");
                    }else if(apiresponse.getData().getKondisi().equals("1")){
                        txtKondisi.setText("Baru");
                    }
                    else{
                        txtKondisi.setText("Baru / Bekas");
                    }
                    if(apiresponse.getData().getInduk()!=null){
                        induk_id = apiresponse.getData().getInduk().getId();
                    }
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
                    if(!sessionManager.getUid().equals(apiresponse.getData().getId_user())){
                        if(!apiresponse.getData().getStatus().equals("0") ){
                            footer.setVisibility(View.VISIBLE);
                        }
                    }
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


    @OnClick(R.id.btnJoin)
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

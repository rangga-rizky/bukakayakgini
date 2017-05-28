package com.example.ranggarizky.bukakayakgini;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.CustomPagerAdapter;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTawaranActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDeskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txtuserCount)
    TextView txtuserCount;
    @BindView(R.id.txtHarga)
    TextView txtHarga;
    @BindView(R.id.txtKondisi)
    TextView txtKondisi;
    @BindView(R.id.txtStok)
    TextView txtStok;
    @BindView(R.id.txtNamaToko)
    TextView txtNamaToko;
    @BindView(R.id.txtKota)
    TextView txtKota;
    @BindView(R.id.level)
    ImageView level;
    @BindView(R.id.btnTandai)
    Button btnTandai;
    @BindView(R.id.txtLevel)
    TextView txtLevel;
    @BindView(R.id.imgAva)
    ImageView imgAva;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.btnHapusTanda)
    Button btnHapusTanda;

    private SessionManager sessionManager;
    private String id,user_id,tawaran_id,tawaran_status;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tawaran);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sessionManager = new SessionManager(this);
        id = getIntent().getStringExtra("id");
        tawaran_status = getIntent().getStringExtra("tawaran_status");
        tawaran_id = getIntent().getStringExtra("tawaran_id");;
        Log.d("tes_id",tawaran_id);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        user_id = getIntent().getStringExtra("user_id");

        if(sessionManager.getUid().equals(user_id)){
            if(tawaran_status.equals("3")){
                btnHapusTanda.setVisibility(View.VISIBLE);
            }else{
                btnTandai.setVisibility(View.VISIBLE);
            }
        }


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

        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getProdukbyID(id+".json");

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {

                ResponseObject apiresponse = response.body();

                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(response.body().getProduct().getCondition().equals("new")){
                        txtKondisi.setText("Baru");
                    }else{
                        txtKondisi.setText("Bekas");
                    }

                    txtStok.setText(response.body().getProduct().getStock());
                    txtDeskripsi.setText(Html.fromHtml(response.body().getProduct().getDesc()));
                    if(response.body().getProduct().getSeller_level().length()==0){
                        txtLevel.setVisibility(View.GONE);
                    }else{
                        txtLevel.setText(response.body().getProduct().getSeller_level());

                    }
                    txtTitle.setText(response.body().getProduct().getName());
                    txtuserCount.setText("("+response.body().getProduct().getRating().getUser_count()+")");
                    txtNamaToko.setText(response.body().getProduct().getSeller_name());
                    txtKota.setText(response.body().getProduct().getCity());
                    rating.setRating(response.body().getProduct().getRating().getAverage_rate());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(apiresponse.getProduct().getPrice())));
                    Picasso.with(getApplicationContext())
                            .load(response.body().getProduct().getSeller_avatar())
                            .placeholder(R.drawable.dummy)
                            .into(imgAva);
                    if(response.body().getProduct().getSeller_level_badge_url().length() > 0){

                        Picasso.with(getApplicationContext())
                                .load(response.body().getProduct().getSeller_level_badge_url())
                                .placeholder(R.drawable.dummy)
                                .into(level);
                    }else{
                        level.setVisibility(View.GONE);
                    }

                    mCustomPagerAdapter = new CustomPagerAdapter(getApplicationContext(),response.body().getProduct().getImages());
                    mPager.setAdapter(mCustomPagerAdapter);
                    indicator.setViewPager( mPager);
                }

            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.btnTandai)
    public void tandai (View view){
        updateStatusTawaran("3");
    }

    @OnClick(R.id.btnHapusTanda)
    public void hapuSTanda (View view){
        updateStatusTawaran("1");
    }

    private void updateStatusTawaran(final String status){
        progressDialog.show();
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseApi> call = apiService.updateStatusTawaran(tawaran_id,status,"ant0k");

        //proses call
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                progressDialog.dismiss();
                ResponseApi apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getSuccess().equals("1")){
                        if(status.equals("2")){
                            Toast.makeText(getApplicationContext(), "Tawaran Berhasil direject", Toast.LENGTH_SHORT).show();
                        }else if(status.equals("3")){
                            Toast.makeText(getApplicationContext(), "Tawaran Berhasil ditandai", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan coba lain kali", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                // Log error
                progressDialog.dismiss();
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.btnPilih)
    public void pilih (View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm);
        TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        txtJudul.setText("Pilih Tawaran");
        txtDeskripsi.setText("Apakah Kamu yakin memberli barang ini ?");
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                insertToCard();
            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void insertToCard(){
        progressDialog.show();
        API apiService = API.client.create(API.class);
        BarangBukaLapak produk = new BarangBukaLapak();
        produk.setId(id);
        produk.setQuantity("1");
        String auth = sessionManager.getUid()+":"+sessionManager.getToken();
        Base64Converter base64Converter = new Base64Converter();
        auth = "basic "+base64Converter.encodeString(auth);
        Call<ResponseObject> call = apiService.addToCart(auth,id+".json",produk);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                progressDialog.dismiss();
                ResponseObject apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getStatus().equals("OK")){
                        Toast.makeText(getApplicationContext(), "Barang telah ditambahkan ke keranjang bukalapak anda", Toast.LENGTH_SHORT).show();
                        openRedirectDialog();
                    }else{
                        Toast.makeText(getApplicationContext(), apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Log error
                progressDialog.dismiss();
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bukaKeranjang(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.bukalapak.com/cart/carts"));
        startActivity(i);
    }

    private void openRedirectDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm);
        TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        btnTerima.setText("OK, Mengerti");
        txtJudul.setText("Piih Tawaran Sukses");
        txtDeskripsi.setText("Kamu akan diarahkan ke halaman keranjang Bukalapak untuk proses transaksinya.");
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bukaKeranjang();
                dialog.dismiss();

            }
        });
        btnBatal.setVisibility(View.GONE);
        dialog.show();
    }


}

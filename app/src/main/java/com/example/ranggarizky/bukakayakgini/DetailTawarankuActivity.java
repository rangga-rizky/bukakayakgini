package com.example.ranggarizky.bukakayakgini;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.CustomPagerAdapter;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTawarankuActivity extends AppCompatActivity {
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
    @BindView(R.id.txtLevel)
    TextView txtLevel;
    @BindView(R.id.imgAva)
    ImageView imgAva;
    @BindView(R.id.rating)
    RatingBar rating;
    private SessionManager sessionManager;
    private String id,user_id,tawaran_id;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tawaranku);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sessionManager = new SessionManager(this);
        id = getIntent().getStringExtra("id");
       // tawaran_id = getIntent().getStringExtra("tawaran_id");;
        //Log.d("tes_id",tawaran_id);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        user_id = getIntent().getStringExtra("user_id");

        loadData();
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
                    loadData();
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

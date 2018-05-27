package com.example.ranggarizky.bukakayakgini;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.adapter.BarangDipilihRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.model.ResponseTawaran;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRequestKuActivity extends AppCompatActivity {
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
    @BindView(R.id.txtExpired)
    TextView txtExpired;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.btnBayar)
    Button btnBayar;
    @BindView(R.id.txtDeskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txtKondisi)
    TextView txtKondisi;
    @BindView(R.id.txtJumlah)
    TextView txtJumlah;
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
    @BindView(R.id.btnLihatTawaran)
    Button btnLihatTawaran;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.layoutDipilih)
    LinearLayout layoutDipilih;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private BarangDipilihRecyclerAdapter mAdapter;
    private String id,title,imgUrl;
    private SessionManager sessionManager;
    private ArrayList<Tawaran> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_ku);
        ButterKnife.bind(this);
        sessionManager =  new SessionManager(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id = getIntent().getStringExtra("id");
        datalist = new ArrayList<>();
        datalist.add(new Tawaran());
        datalist.add(new Tawaran());
        datalist.add(new Tawaran());
        mAdapter = new BarangDipilihRecyclerAdapter(this,datalist);
        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        loadData();
        cekTrans();

    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_item_close:
                openCloseDialog();
                return true;
            case R.id.menu_item_share:
                shareItem(imgUrl);
                return true;
            case R.id.menu_item_edit:
                edit();
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
                    title = apiresponse.getData().getNama();
                    txtTitle.setText(title);
                    txtJumlah.setText(apiresponse.getData().getJumlah());
                    txtJumlahTawaran.setText(String.valueOf(apiresponse.getData().getSupplies().size()));
                    txtKategori.setText(apiresponse.getData().getKategori_detail().getNama());
                    txtExpired.setText(apiresponse.getData().getExpired_at());
                    if(apiresponse.getData().getKondisi().equals("0")){
                        txtKondisi.setText("Bekas");
                    }else if(apiresponse.getData().getKondisi().equals("1")){
                        txtKondisi.setText("Baru");
                    }
                    else{
                        txtKondisi.setText("Baru / Bekas");
                    }
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    txtbudget.setText("Rp"+rupiahFormat.format(Double.parseDouble(apiresponse.getData().getHarga())));
                    txtDeskripsi.setText(apiresponse.getData().getDeskripsi());
                    imgUrl = apiresponse.getData().getFoto();
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getFoto())
                            .placeholder(R.drawable.dummy)
                            .into(imgBarang);
                    if(apiresponse.getData().getSupplies().size() > 0){
                        footer.setVisibility(View.VISIBLE);
                    }
                    if(apiresponse.getData().getStatus().equals("0")){
                        txtSelesai.setTextColor(getResources().getColor(R.color.white));
                        txtSelesai.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_selesai));
                        selesai.setImageDrawable(getResources().getDrawable(R.drawable.circle_selesai));
                    }else if((apiresponse.getData().getStatus().equals("2"))){
                        txtTransksi.setTextColor(getResources().getColor(R.color.white));
                        txtTransksi.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rectangle_accent));
                        transaksi.setImageDrawable(getResources().getDrawable(R.drawable.circle_accent));
                        selesai.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey_non));
                        ditawari.setImageDrawable(getResources().getDrawable(R.drawable.circle_grey));
                        txtDitawari.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_none));
                        txtDitawari.setTextColor(getResources().getColor(R.color.grey));
                        loadSsupplies();;

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

                }

            }


            @Override
            public void onFailure(Call<ResponsePermintaanSingle> call, Throwable t) {
                // Log error
               Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shareItem(String url) {
    Picasso.with(getApplicationContext()).load(url).into(new Target() {
        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(android.content.Intent.EXTRA_TEXT,"Sedang mencari "+title);
            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
            startActivity(Intent.createChooser(i, "Share Image"));
        }
        @Override public void onBitmapFailed(Drawable errorDrawable) { }
        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
    });
    }

    private void loadSsupplies(){
        btnBayar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseTawaran> call = apiService.fetchSupplySelected(id,"ant0k","4");

        //proses call
        call.enqueue(new Callback<ResponseTawaran>() {
            @Override
            public void onResponse(Call<ResponseTawaran> call, Response<ResponseTawaran> response) {

                recyclerView.setVisibility(View.VISIBLE);
                ResponseTawaran apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("tawaranlist", "no response");
                } else {
                    datalist.clear();
                    datalist.addAll(apiresponse.getData());
                    if(apiresponse.getData().size() <= 0) {
                        layoutDipilih.setVisibility(View.GONE);
                    }else{
                        layoutDipilih.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseTawaran> call, Throwable t) {
                // Log error
                Log.e("tawaranlist", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Convert Bitmap into Uri
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    @OnClick(R.id.btnLihatTawaran)
    public void lihat(View view){
        Intent intent = new Intent(this,TawaranListActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    @OnClick(R.id.btnBayar)
    public void bayar(View view){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.bukalapak.com/cart/carts"));
        startActivity(i);
    }



    private void openCloseDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm);
        TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        txtJudul.setText("Tutup Request");
        txtDeskripsi.setText("Apakah Kamu yakin ingin menutup request ?");
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
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



   //edit
    public void edit(){
        Intent intent = new Intent(this,EditRequestActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    //close
    public void close(){
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseApi> call = apiService.closeTawaran(id,"0","ant0k");

        //proses call
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                ResponseApi apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getSuccess().equals("1")){
                        Toast.makeText(getApplicationContext(), "Request Berhasil ditutup", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan coba lain kali", Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.

        getMenuInflater().inflate(R.menu.detail_request_menu, menu);
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}

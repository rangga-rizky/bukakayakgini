package com.example.ranggarizky.bukakayakgini;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
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
    @BindView(R.id.textUserJoin)
    TextView textUserJoin;
    @BindView(R.id.btnPopup)
    ImageButton btnPopup;
    @BindView(R.id.btnJoinRequest)
    ImageButton btnJoinRequest;
    @BindView(R.id.btnLihatTawaran)
    Button btnLihatTawaran;

    private String id,status_join,title,imgUrl;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_ku);
        ButterKnife.bind(this);
        status_join = getIntent().getStringExtra("status_join");
        sessionManager =  new SessionManager(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id = getIntent().getStringExtra("id");
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
                    title = apiresponse.getData().getNama();
                    txtTitle.setText(title);
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
                    txtUserJoin.setText(apiresponse.getData().getJumlah_join());
                    txtStatus.setText(apiresponse.getData().getStatus_caption());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    txtbudget.setText("Rp"+rupiahFormat.format(Double.parseDouble(apiresponse.getData().getHarga())));
                    txtDeskripsi.setText(apiresponse.getData().getDeskripsi());
                    imgUrl = apiresponse.getData().getFoto();
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getFoto())
                            .placeholder(R.drawable.dummy)
                            .into(imgBarang);
                   if(apiresponse.getData().getStatus().equals("1") && status_join.equals("0")){
                       btnPopup.setVisibility(View.VISIBLE);
                       btnJoinRequest.setVisibility(View.VISIBLE);
                    }
                    if(apiresponse.getData().getSupplies().size() > 0){
                        btnLihatTawaran.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.btnJoinRequest)
    public void lihatJoinRequest(View view){
        Intent intent = new Intent(this,RequestJoinActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    @OnClick(R.id.btnPopup)
    public void popUp(View view){
        PopupMenu popup = new PopupMenu(this, btnPopup, Gravity.RIGHT);
        popup.getMenuInflater().inflate(R.menu.request_pop_up_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:edit();break;
                    case R.id.action_close:openCloseDialog();break;
                    case R.id.action_share:shareItem(imgUrl);break;
                }

                return true;
            }
        });

        popup.show();

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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        if(status_join.equals("0")){
            getMenuInflater().inflate(R.menu.detail_request_menu_withjoin, menu);
        }else{
            getMenuInflater().inflate(R.menu.detail_request_menu, menu);
        }
        return true;
    }*/




}

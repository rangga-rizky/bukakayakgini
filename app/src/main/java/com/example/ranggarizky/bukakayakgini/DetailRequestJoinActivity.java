package com.example.ranggarizky.bukakayakgini;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.Join;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.Validation;
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

public class DetailRequestJoinActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.imgBarang)
    ImageView imgBarang;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.btnPopup)
    ImageButton btnPopup;
    @BindView(R.id.btnJoin)
    Button btnJoin;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.txtDeskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txtKondisi)
    TextView txtKondisi;
    @BindView(R.id.txtbudget)
    TextView txtbudget;
    @BindView(R.id.txtKategori)
    TextView txtKategori;
    @BindView(R.id.txtTawaran)
    TextView txtTawaran;
    @BindView(R.id.txtUserJoin)
    TextView txtUserJoin;

    private String id,title;
    private SessionManager sessionManager;

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
        btnPopup.setVisibility(View.GONE);

        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("tes","tes");
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
                    txtTawaran.setText(String.valueOf(apiresponse.getData().getSupplies().size()));
                    txtStatus.setText(apiresponse.getData().getStatus_caption());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    txtbudget.setText("Rp"+rupiahFormat.format(Double.parseDouble(apiresponse.getData().getHarga())));
                    txtDeskripsi.setText(apiresponse.getData().getDeskripsi());
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getFoto())
                            .placeholder(R.drawable.dummy)
                            .into(imgBarang);
                    if(!sessionManager.getUid().equals(apiresponse.getData().getId_user())){
                        if(!apiresponse.getData().getStatus().equals("0") ){
                            if(response.body().getUserIsJoin().equals("0")){
                                btnJoin.setVisibility(View.VISIBLE);
                            }else{
                                btnJoin.setVisibility(View.VISIBLE);
                                btnJoin.setEnabled(false);
                                btnJoin.setText("Kamu Sudah Request join pada request ini");
                            }

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        return false;
    }

    @OnClick(R.id.btnJoin)
    public  void join(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_join_request);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        Button btnJoinDialog = (Button) dialog.findViewById(R.id.btnJoinDialog);
        final EditText editpesan = (EditText) dialog.findViewById(R.id.editPesan);
        final TextView txtTitleDialog = (TextView) dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText("Request Join \" "+title+" \" ");
        btnJoinDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertJoin(editpesan.getText().toString());
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        dialog.show();
    }

    private  void insertJoin(String pesan){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Join join = new Join();
        join.setSecret("ant0k");
        join.setId_buyer(sessionManager.getUid());
        join.setId_demand(id);
        join.setPesan(pesan);
        Call<ResponseApi> call = apiService.join(join);

        //proses call
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                if(response.body().getSuccess().equals("1")){
                        progressDialog.dismiss();
                        setResult(1);
                        finish();
                        Toast.makeText(getApplicationContext(), "Request berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    }


            }


            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}

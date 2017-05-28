package com.example.ranggarizky.bukakayakgini;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.Kota;
import com.example.ranggarizky.bukakayakgini.model.Provinsi;
import com.example.ranggarizky.bukakayakgini.model.ResponseCity;
import com.example.ranggarizky.bukakayakgini.model.ResponseProvinsi;
import com.example.ranggarizky.bukakayakgini.model.ResponseTawaran;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterTawaranActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.checkBaru)
    CheckBox checkBaru;
    @BindView(R.id.checkBekas)
    CheckBox checkBekas;
    @BindView(R.id.checkTertandai)
    CheckBox checkTertandai;
    @BindView(R.id.lokasi)
    CheckBox lokasi;
    @BindView(R.id.spinnerWilayah)
    Spinner spinnerWilayah;
    @BindView(R.id.spinnerKota)
    Spinner spinnerKota;
    @BindView(R.id.layoutLokasi)
    RelativeLayout layoutLokasi;
    private List<String> spinnerProvinceArray,spinnerCityArray;
    private ArrayAdapter<String> adapterProvince,adapterCity;
    private List<Provinsi> provinces;
    private ArrayList<Kota> cities;
    ProgressDialog progressDialog;
    private boolean tandai,baru,bekas,lokasiAktif;
    private String idKota,idProvinsi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tawaran);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        idProvinsi = getIntent().getStringExtra("idProvinsi");
        idKota = getIntent().getStringExtra("idKota");
        tandai = getIntent().getBooleanExtra("tandai",false);
        baru = getIntent().getBooleanExtra("baru",false);
        bekas = getIntent().getBooleanExtra("bekas",false);
        lokasiAktif = getIntent().getBooleanExtra("lokasiAktif",false);

        if(baru){
            checkBaru.setChecked(true);
        }
        if(tandai){
            checkTertandai.setChecked(true);
        }
        if(bekas){
            checkBekas.setChecked(true);
        }
        if(lokasiAktif){
            lokasi.setChecked(true);
        }

        provinces = new ArrayList<>();
        cities = new ArrayList<>();
        spinnerProvinceArray =  new ArrayList<String>();
        adapterProvince = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerProvinceArray);
        adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWilayah.setAdapter(adapterProvince);

        spinnerCityArray =  new ArrayList<String>();
        adapterCity = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerCityArray);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKota.setAdapter(adapterCity);

        spinnerWilayah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadCity(provinces.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* lokasi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    layoutLokasi.setVisibility(View.VISIBLE);
                }else{
                    layoutLokasi.setVisibility(View.INVISIBLE);
                }
            }
        })*/

        loadProvinsi();
    }

    private void loadProvinsi(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseProvinsi> call = apiService.getProvince("ant0k");

        //proses call
        call.enqueue(new Callback<ResponseProvinsi>() {
            @Override
            public void onResponse(Call<ResponseProvinsi> call, Response<ResponseProvinsi> response) {
                if(response.body().getData().size() > 0){
                    provinces = response.body().getData();
                    for(int i=0 ;i < response.body().getData().size() ;i++ ){
                        spinnerProvinceArray.add(response.body().getData().get(i).getNama());
                    }
                    adapterProvince.notifyDataSetChanged();
                    if(idProvinsi!=null){
                        spinnerWilayah.setSelection(Integer.valueOf(idProvinsi));
                    }
                    loadCity(response.body().getData().get(0).getId());
                }else{
                    progressDialog.show();
                }

            }


            @Override
            public void onFailure(Call<ResponseProvinsi> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCity(String id_province){

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseCity> call = apiService.getCity("ant0k",id_province);

        //proses call
        call.enqueue(new Callback<ResponseCity>() {
            @Override
            public void onResponse(Call<ResponseCity> call, Response<ResponseCity> response) {
                progressDialog.dismiss();
                if(response.body().getData().size() > 0){
                    cities.clear();
                    cities.addAll(response.body().getData());
                    spinnerCityArray.clear();
                    adapterCity.clear();
                    for(int i=0 ;i < response.body().getData().size() ;i++ ){
                        spinnerCityArray.add(response.body().getData().get(i).getNama());
                    }
                    adapterCity.notifyDataSetChanged();
                    if(idKota!=null){
                        spinnerKota.setSelection(Integer.valueOf(idKota));
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseCity> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                progressDialog.dismiss();
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

    @OnClick(R.id.btnReset)
    public void reset(View view){
        checkBaru.setChecked(false);
        checkBekas.setChecked(false);
        checkTertandai.setChecked(false);
        lokasi.setChecked(false);
        layoutLokasi.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btnTerapkan)
    public void terapkan(View view){
        idKota = String.valueOf(spinnerKota.getSelectedItemPosition());
        idProvinsi = String.valueOf(spinnerKota.getSelectedItemPosition());
        Intent intent = new Intent();
        intent.putExtra("idKota",idKota);
        intent.putExtra("idProvinsi",idProvinsi);
        intent.putExtra("bekas_checked",checkBekas.isChecked());
        intent.putExtra("baru_checked",checkBaru.isChecked());
        intent.putExtra("bekas_checked",checkBekas.isChecked());
        intent.putExtra("tertandai_checked",checkTertandai.isChecked());
        intent.putExtra("lokasiAktif",lokasi.isChecked());
        if(lokasi.isChecked()){
            intent.putExtra("kota",spinnerKota.getSelectedItem().toString());
        }else{
            intent.putExtra("kota","");
        }
        setResult(1,intent);
        finish();
    }
}

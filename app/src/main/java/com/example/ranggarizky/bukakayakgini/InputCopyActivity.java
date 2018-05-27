package com.example.ranggarizky.bukakayakgini;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.Validation;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputCopyActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtKategori)
    TextView txtKategori;
    @BindView(R.id.editNama)
    EditText editNama;
    @BindView(R.id.editBudget)
    EditText editBudget;
    @BindView(R.id.editDeskripsi)
    EditText editDeskripsi;
    @BindView(R.id.jumlah)
    Button txt_jumlah;
    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @BindView(R.id.cbekas)
    CheckBox cbekas;
    @BindView(R.id.cbaru)
    CheckBox cbaru;
    @BindView(R.id.wrapperNamabarang)
    TextInputLayout wrapperNamabarang;
    @BindView(R.id.wrapperDeskripsi)
    TextInputLayout wrapperDeskripsi;
    @BindView(R.id.wrapperBudget)
    TextInputLayout wrapperBudget;
    @BindView(R.id.btnRequest)
    Button btnRequest;
    private SessionManager sessionManager;
    private String id,id_kategori,kondisi,induk_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_request);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        induk_id = getIntent().getStringExtra("induk_id");
        sessionManager = new SessionManager(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnRequest.setText("AKU JUGA MAU");
        loadData();
    }

    @OnClick(R.id.kategori)
    public void selectKategori(View view){
        startActivityForResult(new Intent(this,CategoryActivity.class),1);
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
                    editNama.setText(apiresponse.getData().getNama());
                    txtKategori.setText(apiresponse.getData().getKategori_detail().getNama());
                    id_kategori = apiresponse.getData().getKategori_detail().getId();
                    editBudget.setText(apiresponse.getData().getHarga());
                    editDeskripsi.setText(apiresponse.getData().getDeskripsi());
                    txt_jumlah.setText(apiresponse.getData().getJumlah());
                    Picasso.with(getApplicationContext())
                            .load(apiresponse.getData().getFoto())
                            .placeholder(R.drawable.dummy)
                            .into(imgFoto);


                    if(apiresponse.getData().getKondisi().equals("1")){
                        cbaru.setChecked(true);
                    }else if(apiresponse.getData().getKondisi().equals("0")){
                        cbekas.setChecked(true);
                    }else{
                        cbaru.setChecked(true);
                        cbekas.setChecked(true);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            txtKategori.setText(data.getStringExtra("nama_kategori"));
            id_kategori = data.getStringExtra("id_kategori");
        }
    }

    @OnClick(R.id.btnRequest)
    public void btnRequest(View view){
        if(cbaru.isChecked()){
            kondisi = "1";
        }else if(cbekas.isChecked()){
            kondisi = "0";
        }else if(cbaru.isChecked() && cbekas.isChecked()){
            kondisi = "2";
        }

        if(Validation.checkEmpty(wrapperNamabarang,editNama) && Validation.checkEmpty(wrapperBudget,editBudget) &&
                Validation.checkEmpty(wrapperDeskripsi,editDeskripsi ) ){

            if(kondisi == null){
                Toast.makeText(getApplicationContext(),"Harap pilih minimal satu kondisi",Toast.LENGTH_SHORT).show();

            }else{
                if(id_kategori==null){
                    Toast.makeText(this,"Harap pilih Kategori",Toast.LENGTH_SHORT).show();
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.show();
                    WEB_API apiService = WEB_API.client.create(WEB_API.class);

                    MultipartBody.Part body = null;
                    RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"),   sessionManager.getUid());
                    RequestBody nama = RequestBody.create(MediaType.parse("multipart/form-data"), editNama.getText().toString());
                    RequestBody deskripsi = RequestBody.create(MediaType.parse("multipart/form-data"), editDeskripsi.getText().toString());
                    RequestBody harga = RequestBody.create(MediaType.parse("multipart/form-data"), editBudget.getText().toString());
                    RequestBody jumlah = RequestBody.create(MediaType.parse("multipart/form-data"), txt_jumlah.getText().toString());
                    RequestBody kategori  = RequestBody.create(MediaType.parse("multipart/form-data"), id_kategori);
                    RequestBody kondisiBarang  = RequestBody.create(MediaType.parse("multipart/form-data"), kondisi);
                    RequestBody secret  = RequestBody.create(MediaType.parse("multipart/form-data"), "ant0k");
                    RequestBody id_asli  = RequestBody.create(MediaType.parse("multipart/form-data"), induk_id);


                    Call<ResponseApi> call = apiService.copyPermintaan(
                            uid,
                            id_asli,
                            nama,
                            deskripsi,
                            harga,
                            kategori,
                            secret,
                            kondisiBarang,
                            jumlah,
                            body
                    );

                    //proses call
                    call.enqueue(new Callback<ResponseApi>() {
                        @Override
                        public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                            //merubah respon body menjadi model

                            progressDialog.dismiss();
                            if(response.body() == null){
                                Log.e("cok","Rating no response");
                            }
                            else {
                                if(response.body().getSuccess()!=null){
                                    Toast.makeText(getApplicationContext(),"Permintaan Berhasil di copy",Toast.LENGTH_SHORT).show();
                                    openSuccessDialog(response.body().getSaved().getId());
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseApi> call, Throwable t) {
                            // Log error
                            Log.e("cok", "onFailure: ", t.fillInStackTrace());
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        }

    }

    private void openSuccessDialog(final String id){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm);
        TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        btnTerima.setText("OK, Mengerti");
        txtJudul.setText("Request Berhasil");
        txtDeskripsi.setText("Kamu akan dikabari melalui notif ketika ada pelapak menawarkan barang kepadamu.");btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DetailRequestKuActivity.class);
                intent.putExtra("id",id);

                startActivity(intent);
                finish();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DetailRequestKuActivity.class);
                intent.putExtra("id",id);

                startActivity(intent);
                finish();
            }
        });
        btnBatal.setVisibility(View.GONE);
        dialog.show();
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

    @OnClick(R.id.btnPlus)
    public void plusQty(View view){
        txt_jumlah.setText(String.valueOf(Integer.valueOf(txt_jumlah.getText().toString()) + 1));
    }

    @OnClick(R.id.btnMinus)
    public void minusQty(View view){
        if(Integer.valueOf(txt_jumlah.getText().toString()) > 1){
            txt_jumlah.setText(String.valueOf(Integer.valueOf(txt_jumlah.getText().toString()) - 1));
        }
    }
}

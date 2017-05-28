package com.example.ranggarizky.bukakayakgini;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.util.BitmapScaler;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.Validation;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRequestActivity extends AppCompatActivity {
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
    private File file;
    private Bitmap bitmap;
    private Uri filePath;
    private String kondisi;
    private  SessionManager sessionManager;
    private String id,id_kategori;
    private int PICK_CAMERA_REQUEST=99,PICK_IMAGE_REQUEST=91;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_request);
        ButterKnife.bind(this);
        editNama.setEnabled(false);
        id = getIntent().getStringExtra("id");
        sessionManager = new SessionManager(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        loadData();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

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
            //Toast.makeText(this,id_kategori,Toast.LENGTH_SHORT).show();
        }

        if(requestCode == PICK_CAMERA_REQUEST && resultCode == RESULT_OK ){


            /*Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String todayDate = dateFormat.format(new Date());

            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + todayDate + ".jpg");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgFoto.setImageBitmap(photo);*/
            file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
            Uri takenPhotoUri = Uri.fromFile(file);
// by this point we have the camera photo on disk
            Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
// See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
            Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 300);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
// Compress the image further
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
// Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String todayDate = dateFormat.format(new Date());

            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + todayDate + ".jpg");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgFoto.setImageBitmap(resizedBitmap);
        }else  if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picasso.with(getApplicationContext())
                        .load(filePath)
                        .placeholder(R.drawable.dummy)
                        .into(imgFoto);
                file = new File(getPath(filePath));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

    @OnClick(R.id.imgFoto)
    public void openCamera(View view){
        requestStoragePermission();
        CharSequence item[] = new CharSequence[] {"Kamera", "Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    openCamera();
                }else{
                    showFileChooser();
                }
            }
        });
        builder.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    public void openCamera(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(cameraIntent, PICK_CAMERA_REQUEST);

            Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
            photo.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(photo,PICK_CAMERA_REQUEST);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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
                    if(file!=null){
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        body = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);
                    }
                    RequestBody nama = RequestBody.create(MediaType.parse("multipart/form-data"), editNama.getText().toString());
                    RequestBody deskripsi = RequestBody.create(MediaType.parse("multipart/form-data"), editDeskripsi.getText().toString());
                    RequestBody harga = RequestBody.create(MediaType.parse("multipart/form-data"), editBudget.getText().toString());
                    RequestBody kategori  = RequestBody.create(MediaType.parse("multipart/form-data"), id_kategori);
                    RequestBody kondisiBarang  = RequestBody.create(MediaType.parse("multipart/form-data"), kondisi);
                    RequestBody secret  = RequestBody.create(MediaType.parse("multipart/form-data"), "ant0k");
                    RequestBody id_request  = RequestBody.create(MediaType.parse("multipart/form-data"), id);


                    Call<ResponseApi> call = apiService.updatePermintaan(
                            id_request,
                            deskripsi,
                            harga,
                            kategori,
                            secret,
                            kondisiBarang,
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
                                    Toast.makeText(getApplicationContext(),"Permintaan Berhasil di kirim",Toast.LENGTH_SHORT).show();
                                    finish();
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}

package com.example.ranggarizky.bukakayakgini;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.User;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.Validation;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.facebook.CallbackManager;
import com.xwray.passwordview.PasswordView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{
    @BindView(R.id.editUsername)
    EditText editUsername;
    @BindView(R.id.editPassword)
    PasswordView editPassword;
    @BindView(R.id.wrapperPassword)
    TextInputLayout wrapperPassword;
    @BindView(R.id.wrapperUsername)
    TextInputLayout wrapperUsername;

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    int RC_SIGN_IN =1;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick(R.id.imgBack)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.btnLogin)
    public void toLogin(View view){
        if(Validation.checkEmpty(wrapperUsername,editUsername) && Validation.checkPassword(this,editPassword)){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            String auth = editUsername.getText().toString()+":"+editPassword.getText().toString();
            Base64Converter base64Converter = new Base64Converter();
            auth = "basic "+base64Converter.encodeString(auth);
            API apiService = API.client.create(API.class);
            Call<User> call = apiService.login(auth);

            //proses call
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User apiresponse = response.body();
                    if(apiresponse.getStatus().equals("OK")){

                        sessionManager.setEmail(apiresponse.getEmail());
                        sessionManager.setUsername(apiresponse.getUser_name());
                        sessionManager.setTOken(apiresponse.getToken());
                        sessionManager.setUid(apiresponse.getUser_id());
                        sessionManager.setLogin(true);
                        insertUser(apiresponse.getUser_id(),apiresponse.getUser_name(),apiresponse.getEmail());

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),apiresponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("cok", "onFailure: ", t.fillInStackTrace());
                    Toast.makeText(getApplicationContext(),"Failed to Connect Internet",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void insertUser(String uid,String nama,String email){
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseApi> call = apiService.storeUser(uid,nama,sessionManager.getFirebaseToken(),email,nama,"ant0k");

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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    @OnClick(R.id.btnDaftar)
    public void daftar(View view){
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra("url","https://www.bukalapak.com/register");
        intent.putExtra("title","DAFTAR BUKALAPAK");
        startActivity(intent);
    }

}


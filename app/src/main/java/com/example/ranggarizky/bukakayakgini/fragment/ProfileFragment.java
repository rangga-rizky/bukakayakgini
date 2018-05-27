package com.example.ranggarizky.bukakayakgini.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.FirstActivity;
import com.example.ranggarizky.bukakayakgini.MainActivity;
import com.example.ranggarizky.bukakayakgini.ProfileActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.SubscribeCategoryActivity;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.model.ResponseProfile;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private SessionManager sessionManager;
    @BindView(R.id.txtJumlahRequest)
    TextView txtJumlahRequest;
    @BindView(R.id.txtJumlahMenawarkan)
    TextView txtJumlahMenawarkan;
    @BindView(R.id.txtJumlahTransaksi)
    TextView txtJumlahTransaksi;
    @BindView(R.id.txtKota)
    TextView txtKota;
    @BindView(R.id.txtNama)
    TextView txtNama;
    @BindView(R.id.imgAva)
    ImageView imgAva;
    @BindView(R.id.box1)
    RelativeLayout box1;
    @BindView(R.id.box2)
    RelativeLayout box2;
    @BindView(R.id.box3)
    RelativeLayout box3;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,view);
        sessionManager = new SessionManager(getActivity());
        txtNama.setText(sessionManager.getUsername());
        txtKota.setText(sessionManager.getLokasi());
        loadData();
        loadUser();

        return view;
    }

    private void loadData(){

        progressBar.setVisibility(View.VISIBLE);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseProfile> call = apiService.getuserStat(sessionManager.getUid(),"ant0k");

        //proses call
        call.enqueue(new Callback<ResponseProfile>() {
            @Override
            public void onResponse(Call<ResponseProfile> call, Response<ResponseProfile> response) {

                progressBar.setVisibility(View.GONE);
                box1.setVisibility(View.VISIBLE);
                box2.setVisibility(View.VISIBLE);
                box3.setVisibility(View.VISIBLE);
                ResponseProfile apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    txtJumlahRequest.setText(apiresponse.getData().getNumDemand()+ " kali request barang");
                    txtJumlahTransaksi.setText(apiresponse.getData().getNumGotDeal()+ " kali barang dipilih");
                    txtJumlahMenawarkan.setText(apiresponse.getData().getNumSupply() + " kali menawarkan barang");

                }
            }

            @Override
            public void onFailure(Call<ResponseProfile> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUser(){
        API apiService = API.client.create(API.class);
        String auth = sessionManager.getUid()+":"+sessionManager.getToken();
        Base64Converter base64Converter = new Base64Converter();
        auth = "basic "+base64Converter.encodeString(auth);
        Call<ResponseObject> call = apiService.getUser(auth);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
               if(response.body()!= null){
                        Picasso.with(getActivity())
                                .load(response.body().getUser().getAvatar())
                                .placeholder(R.drawable.dummy)
                                .into(imgAva);
               }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

    @OnClick(R.id.btnLogout)
    public void logout(View view){
        sessionManager.setLogin(false);
        Intent intent = new Intent(getActivity(), FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.btnInterest)
    public void btnInterest(View view){
        startActivity(new Intent(getActivity(), SubscribeCategoryActivity.class));
    }
}

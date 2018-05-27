package com.example.ranggarizky.bukakayakgini.fragment;



import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.BeforeRequestActivity;
import com.example.ranggarizky.bukakayakgini.InputRequestActivity;
import com.example.ranggarizky.bukakayakgini.PrepareRequestActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.adapter.BarangBukaLapakSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.RequestSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeforeRequestFragment extends Fragment {
    private static final String NAMA_BARANG= "nama_barang";
    private String namaBarang;
    @BindView(R.id.horizontal_recycler_view)
    RecyclerView horizontal_recycler_view;
    @BindView(R.id.horizontal_request_recycler_view)
    RecyclerView horizontal_request_recycler_view;
    @BindView(R.id.txtBukaKayaGini)
    TextView txtBukaKayaGini;
    @BindView(R.id.txtBukaLapak)
    TextView txtBukaLapak;

    private Boolean ketemu = false;
    private BarangBukaLapakSquareRecyclerAdapter barangBukaLapakRecyclerAdapter;
    private RequestSquareRecyclerAdapter requestSquareRecyclerAdapter;


    public BeforeRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_before_request, container, false);
        ButterKnife.bind(this,view);
        final Bundle args = getArguments();
        namaBarang = args.getString(NAMA_BARANG);
        horizontal_recycler_view.setVisibility(View.GONE);

        LinearLayoutManager horizontalLayoutManagaerSquare
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_request_recycler_view.setLayoutManager(horizontalLayoutManagaerSquare);
        loadProdukBL(namaBarang);

        return view;
    }

    private void loadProdukBL(String word){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getProduk(word,"1","10");

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                loadRequest();
                if(response.body()!=null){
                    if(response.body().getProducts().size() > 0){

                        txtBukaLapak.setVisibility(View.VISIBLE);
                        horizontal_recycler_view.setVisibility(View.VISIBLE);
                        barangBukaLapakRecyclerAdapter = new BarangBukaLapakSquareRecyclerAdapter(getActivity(),response.body().getProducts());
                        LinearLayoutManager horizontalLayoutManagaer
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
                        horizontal_recycler_view.setAdapter(barangBukaLapakRecyclerAdapter);
                        ketemu = true;
                    }
                }


            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                if(getActivity()!=null){ Toast.makeText(getActivity(),"Failed to Connect Internet",Toast.LENGTH_SHORT).show();}
            }
        });
    }

    private void loadRequest(){

        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponsePermintaan> call = apiService.searchDemand(namaBarang,"ant0k","1","6");

        //proses call

        call.enqueue(new Callback<ResponsePermintaan>() {
            @Override
            public void onResponse(Call<ResponsePermintaan> call, Response<ResponsePermintaan> response) {

                ResponsePermintaan apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getData().size() > 0){
                        ketemu = true;
                        requestSquareRecyclerAdapter = new RequestSquareRecyclerAdapter(getActivity(),apiresponse.getData());
                        horizontal_request_recycler_view.setAdapter(requestSquareRecyclerAdapter);
                        horizontal_request_recycler_view.setVisibility(View.VISIBLE);
                        txtBukaKayaGini.setVisibility(View.VISIBLE);
                    }else{
                        horizontal_request_recycler_view.setVisibility(View.GONE);
                    }
                    if(!ketemu){
                        gotoInputRequest();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponsePermintaan> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btnRequest)
    public void requestAja(View view){
        gotoInputRequest();
    }



    private void gotoInputRequest(){
        Intent intent = new Intent(getActivity(), InputRequestActivity.class);
        intent.putExtra("namaBarang",namaBarang);
        startActivity(intent);
        getActivity().finish();
    }

    public static BeforeRequestFragment newInstance( String name) {
        final Bundle args = new Bundle();
        args.putString(NAMA_BARANG, name);
        final BeforeRequestFragment fragment = new BeforeRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onResume(){
        super.onResume();
        if(getActivity() instanceof PrepareRequestActivity){
            ((PrepareRequestActivity) getActivity())
                    .setActionBarTitle(namaBarang.toUpperCase());
        }

    }



}

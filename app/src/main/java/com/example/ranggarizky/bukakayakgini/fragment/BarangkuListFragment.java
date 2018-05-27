package com.example.ranggarizky.bukakayakgini.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.PrepareRequestActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.TawarkanActivity;
import com.example.ranggarizky.bukakayakgini.WebViewActivity;
import com.example.ranggarizky.bukakayakgini.adapter.BarangBukaLapakRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.BarangBukaLapakSquareRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.adapter.TawaranKuRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarangkuListFragment extends Fragment {
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.search_box)
    EditText search_box;
    @BindView(R.id.txtJumlahBarang)
    TextView txtJumlahBarang;

    private ArrayList<BarangBukaLapak> barang;
    private List<String> idBarangDipilih;
    private SessionManager sessionManager;
    private  BarangBukaLapakRecyclerAdapter mAdapter;
    private static String ID_BARANG ="idbarang",id_permintaan,LIMIT="limit";
    private  int i,limit;

    private onPilihBarang mListener;



    public BarangkuListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barangku_list, container, false);
        ButterKnife.bind(this,view);
        final Bundle args = getArguments();
        id_permintaan = args.getString(ID_BARANG);

        limit = 3 - args.getInt(LIMIT);
        sessionManager = new SessionManager(getActivity());
        barang = new ArrayList<>();
        idBarangDipilih = new ArrayList<>();
        mAdapter = new BarangBukaLapakRecyclerAdapter(getActivity(),barang,this,limit);
        txtJumlahBarang.setText("0 dari maksimal "+String.valueOf(limit)+" barang dipilih");

        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(mAdapter);
        loadProdukBL();

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    searchProdukBL(s.toString());
                }else{
                    loadProdukBL();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return view;
    }

    private void loadProdukBL(){
        API apiService = API.client.create(API.class);
        String auth = sessionManager.getUid()+":"+sessionManager.getToken();
        Base64Converter base64Converter = new Base64Converter();
        auth = "basic "+base64Converter.encodeString(auth);
        Call<ResponseObject> call = apiService.getMyProduct(auth);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null){
                    barang.clear();
                    barang.addAll(response.body().getProducts());
                    mAdapter.notifyDataSetChanged();
                }else{
                    loadProdukBL();
                }


            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                if(getActivity()!=null){ Toast.makeText(getActivity(),"Failed to Connect Internet",Toast.LENGTH_SHORT).show();}
            }
        });
    }

    private void searchProdukBL(String keyword){
        API apiService = API.client.create(API.class);
        String auth = sessionManager.getUid()+":"+sessionManager.getToken();
        Base64Converter base64Converter = new Base64Converter();
        auth = "basic "+base64Converter.encodeString(auth);
        Call<ResponseObject> call = apiService.getMyProduct(auth,keyword);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                barang.clear();
                barang.addAll(response.body().getProducts());
                mAdapter.notifyDataSetChanged();
                Log.d("hahaha",String.valueOf(response.body().getProducts().size()));


            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                if(getActivity()!=null){ Toast.makeText(getActivity(),"Failed to Connect Internet",Toast.LENGTH_SHORT).show();}
            }
        });
    }

    public static BarangkuListFragment newInstance(String id,int limit) {
        final Bundle args = new Bundle();
        args.putString(ID_BARANG, id);
        args.putInt(LIMIT, limit);
        final BarangkuListFragment fragment = new BarangkuListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface onPilihBarang {
        void onPilihBarang(String id_barang);
    }

    public void changeJumlah(String jumlah) {
        txtJumlahBarang.setText(jumlah+" dari maksimal "+String.valueOf(limit)+" barang dipilih");
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof onPilihBarang) {
            mListener = (onPilihBarang) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    @OnClick(R.id.btnBarangBaru)
    public void barangBaru(View view){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_confirm);
        TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        btnTerima.setText("OK, Mengerti");
        txtJudul.setText("Tawaran Barang Baru");
        txtDeskripsi.setText("Untuk menambahkan barang baru anda akan diarahkan ke aplikasi Bukalapak, Setelah kamu menambahkan barang dengan mengupload di BukaLapak, kamu bisa menawarkannya ke calon pembeli di BukaKayakGini.");
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.bukalapak.com/products/new"));
                startActivity(i);
            }
        });
        btnBatal.setVisibility(View.GONE);
        dialog.show();
    }


    @OnClick(R.id.btnTawar)
    public void openDialog(View view){
                tawarkan();
    }

    private void tawarkan(){

        idBarangDipilih = mAdapter.getBarangDipilih();

        if(idBarangDipilih.size() > 0){
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait");
            progressDialog.show();

            for (i = 0; i < idBarangDipilih.size();i++){

                WEB_API apiService = WEB_API.client.create(WEB_API.class);
                Tawaran tawaran = new Tawaran();
                tawaran.setSecret("ant0k");
                tawaran.setId_produk(idBarangDipilih.get(i));
                tawaran.setId_request(id_permintaan);
                tawaran.setId_seller(sessionManager.getUid());
                Call<ResponseApi> call = apiService.tawarkan(tawaran);

                //proses call
                call.enqueue(new Callback<ResponseApi>() {

                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                        if((i-1) ==  (idBarangDipilih.size()-1)){
                            progressDialog.dismiss();
                            getActivity().finish();
                            Toast.makeText(getActivity(), "Tawaran berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        }


                    }


                    @Override
                    public void onFailure(Call<ResponseApi> call, Throwable t) {
                        // Log error
                        Log.e("cok", "onFailure: ", t.fillInStackTrace());

                        if(getActivity()!=null){Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();}
                        progressDialog.dismiss();
                    }
                });
            }

        }else{
            if(getActivity()!=null){
                Toast.makeText(getActivity(),"Anda Belum memilih satupun Barang",Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void onResume(){
        super.onResume();
        ((TawarkanActivity) getActivity())
                .setActionBarTitle("TAWARKAN BARANG");

    }

}

package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.DetailTawarankuActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.fragment.BarangkuListFragment;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class BarangDipilihRecyclerAdapter extends RecyclerView.Adapter<BarangDipilihRecyclerAdapter.MyViewHolder> {

    private List<Tawaran> verticalList;
    private List<String> barangDipilih;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtuserCount)
        TextView txtuserCount;
        @BindView(R.id.content)
        CardView content;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public BarangDipilihRecyclerAdapter(Context context, List<Tawaran> verticalList ){
        this.verticalList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barang_dipilih, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Tawaran item = verticalList.get(position);
        loadBarang(item.getId_produk(),holder);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailTawarankuActivity.class);
                intent.putExtra("id",item.getId_produk());
                context.startActivity(intent);
            }
        });

    }

    private void loadBarang(String id, final MyViewHolder holder){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getProdukbyID(id+".json");

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null){
                    holder.txtTitle.setText(response.body().getProduct().getName());
                    holder.txtuserCount.setText(response.body().getProduct().getRating().getUser_count());
                    holder.rating.setRating(response.body().getProduct().getRating().getAverage_rate());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    holder.txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(response.body().getProduct().getPrice())));
                    Picasso.with(context)
                            .load(response.body().getProduct().getSmall_images().get(0))
                            .placeholder(R.drawable.dummy)
                            .into(holder.imgBarang);

                }

            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

    public List<String> getBarangDipilih() {
        return barangDipilih;
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }



}


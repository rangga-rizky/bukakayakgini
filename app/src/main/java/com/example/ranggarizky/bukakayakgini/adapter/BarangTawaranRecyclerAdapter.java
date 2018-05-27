package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.DetailTawarankuActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
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
public class BarangTawaranRecyclerAdapter extends RecyclerView.Adapter<BarangTawaranRecyclerAdapter.MyViewHolder> {

    private List<Tawaran> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.content)
        LinearLayout content;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public BarangTawaranRecyclerAdapter(Context context, List<Tawaran> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barang_tawaran, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Tawaran item = data.get(position);
        loadBarang(item.getId_produk(),holder);
        switch (item.getStatus()){
            case "3" :
                holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_ditandai));
                holder.txtStatus.setText("Dipertimbangkan");
                break;
            case "4" :
                holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_dipilih));
                holder.txtStatus.setText("Dipilih");
                break;
            case "1" :
               // holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_menunggu));
                holder.txtStatus.setTextColor(context.getResources().getColor(R.color.grey));
                holder.txtStatus.setText("Menunggu");
                break;
            case "2" :
                holder.txtStatus.setText("Diabaikan");
                holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_ignore));
                break;
        }
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

    @Override
    public int getItemCount() {
        return data.size();
    }
}


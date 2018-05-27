package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.DetailRequestActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
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
public class RequestVerticalSquareRecyclerAdapter extends RecyclerView.Adapter<RequestVerticalSquareRecyclerAdapter.MyViewHolder> {

    private List<RequestObject> verticalList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.imgAva)
        ImageView imgAva;
        @BindView(R.id.main_content)
        RelativeLayout main_content;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtNama)
        TextView txtNama;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtJumlahTawaran)
        TextView txtJumlahTawaran;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public RequestVerticalSquareRecyclerAdapter(Context context, List<RequestObject> verticalList) {
        this.verticalList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_square_vertical, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequestObject item = verticalList.get(position);
        holder.txtStatus.setText(item.getStatus_caption());
        switch (item.getStatus()){
            case "0" : holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_selesai));break;
            case "2" : holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_transaksi));break;
            case "1" : if(item.getSupplies().size() > 0){
                holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_ditawari));break;
            }else{
                holder.txtStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.status_menunggu));break;
            }
        }
        if(item.getNama().length() > 21){
            holder.txtTitle.setText(item.getNama().substring(0,20)+"..");
        }else{
            holder.txtTitle.setText(item.getNama());
        }
        holder.txtJumlahTawaran.setText("x "+String.valueOf(item.getSupplies().size()));
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
        holder.txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(item.getHarga())));
        Picasso.with(context)
                .load(item.getFoto())
                .placeholder(R.drawable.dummy)
                .resize(300,300)
                .centerCrop()
                .into(holder.imgBarang);
        loadUser(item.getId_user(),holder.txtNama,holder.imgAva);
        holder.main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRequestActivity.class);
                intent.putExtra("id",item.getId());
                context.startActivity(intent);
            }
        });
    }

    private void loadUser(String id, final TextView txtName,final ImageView imgAva){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getUserbyID(id);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null){
                    if(response.body().getUser() != null){
                        txtName.setText(response.body().getUser().getUsername());
                        Picasso.with(context)
                                .load(response.body().getUser().getAvatar())
                                .placeholder(R.drawable.dummy)
                                .into(imgAva);
                    }
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
        return verticalList.size();
    }
}


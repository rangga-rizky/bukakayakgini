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

import com.example.ranggarizky.bukakayakgini.DetailRequestJoinActivity;
import com.example.ranggarizky.bukakayakgini.DetailRequestKuActivity;
import com.example.ranggarizky.bukakayakgini.PrepareRequestActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class RequestSquareRecyclerAdapter extends RecyclerView.Adapter<RequestSquareRecyclerAdapter.MyViewHolder> {

    private List<RequestObject> datalist;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.imgAva)
        ImageView imgAva;
        @BindView(R.id.txtNama)
        TextView txtNama;
        @BindView(R.id.main_content)
        RelativeLayout main_content;
        @BindView(R.id.txtTitle)
        TextView txtTitle;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public RequestSquareRecyclerAdapter(Context context, List<RequestObject> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_square, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequestObject item = datalist.get(position);
        holder.txtTitle.setText(item.getNama());
        loadUser(item.getId_user(),holder.txtNama,holder.imgAva);
        Picasso.with(context)
                .load(item.getFoto())
                .placeholder(R.drawable.dummy)
                .into(holder.imgBarang);
        holder.main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRequestJoinActivity.class);
                intent.putExtra("id",item.getId());
                ((PrepareRequestActivity)context).startActivityForResult(intent,1);
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
                    txtName.setText(response.body().getUser().getUsername());

                    Picasso.with(context)
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

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}


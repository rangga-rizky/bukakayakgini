package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.example.ranggarizky.bukakayakgini.DetailRequestActivity;
import com.example.ranggarizky.bukakayakgini.DetailTawaranActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class TawaranKuRecyclerAdapter extends RecyclerView.Adapter<TawaranKuRecyclerAdapter.MyViewHolder> {

    private List<RequestObject> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtNama)
        TextView txtNama;
        @BindView(R.id.item_list)
        RecyclerView recyclerView;
        @BindView(R.id.card_view)
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public TawaranKuRecyclerAdapter(Context context, List<RequestObject> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tawaran_ku, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequestObject item = data.get(position);
        holder.txtTitle.setText("\" "+item.getNama()+"\"");
        loadUser(item.getId_user(),holder.txtNama);
        BarangTawaranRecyclerAdapter mAdapter = new BarangTawaranRecyclerAdapter(context,item.getSupplies());

        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(mLayoutManagerOrder);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setVisibility(View.VISIBLE);
        holder.recyclerView.setAdapter(mAdapter);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRequestActivity.class);
                intent.putExtra("id",item.getId());
                context.startActivity(intent);
            }
        });

    }

    private void loadUser(String id, final TextView txtName){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getUserbyID(id);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null){
                    txtName.setText(response.body().getUser().getUsername());
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


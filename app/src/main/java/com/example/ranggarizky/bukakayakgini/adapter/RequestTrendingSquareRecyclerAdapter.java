package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.TrendingRequestListActivity;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class RequestTrendingSquareRecyclerAdapter extends RecyclerView.Adapter<RequestTrendingSquareRecyclerAdapter.MyViewHolder> {

    private List<RequestObject> verticalList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.main_content)
        RelativeLayout main_content;
        @BindView(R.id.txtJumlahRequest)
        TextView txtJumlahRequest;
        @BindView(R.id.txtTitle)
        TextView txtTitle;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public RequestTrendingSquareRecyclerAdapter(Context context, List<RequestObject> verticalList) {
        this.verticalList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_trending, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequestObject item = verticalList.get(position);
        if(item.getNama().length() > 21){
            holder.txtTitle.setText(item.getNama().substring(0,20)+"..");
        }else{
            holder.txtTitle.setText(item.getNama());
        }
        holder.txtJumlahRequest.setText(String.valueOf(item.getJumlah_anak()+1)+" request");
        Picasso.with(context)
                .load(item.getFoto())
                .placeholder(R.drawable.dummy)
                .resize(300,300)
                .centerCrop()
                .into(holder.imgBarang);
        holder.main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrendingRequestListActivity.class);
                intent.putExtra("id_induk",item.getId());
                 context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}


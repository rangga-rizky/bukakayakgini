package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.ProgressDialog;
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

import com.example.ranggarizky.bukakayakgini.DetailRequestKuActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ranggarizky on 5/11/2016.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    OnItemClickListener mItemClickListener;
    private List<RequestObject> datalist;
    private Context context;
    private ProgressDialog progressDialog;
   // private Tracking item;
    int mode;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.utama)
        RelativeLayout layoutUtama;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtDate)
        TextView txtDate;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtKategori)
        TextView txtKategori;
        @BindView(R.id.imgBarang)
        ImageView imgBarang;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }



    public RequestAdapter(Context context, List<RequestObject> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final RequestObject item = datalist.get(position);
        holder.txtDate.setText(item.getTanggal());
        if(item.getNama().length() > 30){
            holder.txtTitle.setText(item.getNama().substring(0,25)+"...");
        }else{
            holder.txtTitle.setText(item.getNama());
        };
        holder.txtKategori.setText(item.getKategori_detail().getNama());
        holder.layoutUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRequestKuActivity.class);
                intent.putExtra("id",item.getId());
                intent.putExtra("status_join",item.getStatus_join());
                context.startActivity(intent);
            }
        });

        holder.txtStatus.setText(item.getStatus_caption());
        Picasso.with(context)
                .load(item.getFoto())
                .placeholder(R.drawable.dummy)
                .into(holder.imgBarang);


    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}


package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.DetailRequestActivity;
import com.example.ranggarizky.bukakayakgini.DetailTawaranActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.Notif;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {

    private List<Notif> datalist;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtDate)
        TextView txtDate;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.main_content)
        LinearLayout main_content;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public NotificationRecyclerAdapter(Context context, List<Notif> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Notif item = datalist.get(position);
        holder.txtDate.setText(item.getTanggal());
        holder.txtTitle.setText(item.getContent());
        holder.main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getId_terkait()!=null){
                    if(item.getOther().equals("newproductsub") || item.getOther().equals("demand_expired")  ){
                        Intent intent = new Intent(context, DetailRequestActivity.class);
                        intent.putExtra("id",item.getId_terkait());
                        context.startActivity(intent);
                    }else if(item.getOther().equals("supply")){
                        Intent intent = new Intent(context, DetailTawaranActivity.class);
                        intent.putExtra("id",item.getId_terkait());
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}


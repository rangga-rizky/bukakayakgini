package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.WebViewActivity;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
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
public class BarangBukaLapakSquareRecyclerAdapter extends RecyclerView.Adapter<BarangBukaLapakSquareRecyclerAdapter.MyViewHolder> {

    private List<BarangBukaLapak> verticalList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtuserCount)
        TextView txtuserCount;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.card_view)
        CardView card_view;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public BarangBukaLapakSquareRecyclerAdapter(Context context, List<BarangBukaLapak> verticalList) {
        this.verticalList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barang_bukalapak, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BarangBukaLapak item = verticalList.get(position);
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
        holder.txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(item.getPrice())));
        if(item.getName().length() > 25){
            holder.txtTitle.setText(item.getName().substring(0,25)+"...");
        }else{
            holder.txtTitle.setText(item.getName());
        }
        holder.txtuserCount.setText("("+item.getRating().getUser_count()+")");
        holder.rating.setRating(item.getRating().getAverage_rate());
        Picasso.with(context)
                .load(item.getSmall_images().get(0))
                .placeholder(R.drawable.dummy)
                .into(holder.imgBarang);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getUrl()));
                context.startActivity(i);
            }
        });


    }



    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}


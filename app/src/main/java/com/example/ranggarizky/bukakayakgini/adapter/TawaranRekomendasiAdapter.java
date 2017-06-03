package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.RequestJoinActivity;
import com.example.ranggarizky.bukakayakgini.model.Join;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ranggarizky on 9/12/2016.
 */
public class TawaranRekomendasiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String urlImage1,urlImage2;
    //private static final int TYPE_FOOTER = 2;

    ArrayList<Tawaran> generics;
    Tawaran dataHeader;
    String jumlahRequest,jumlahJoin;
    Context context;

    public TawaranRekomendasiAdapter(Context context, ArrayList<Tawaran> generics,Tawaran dataHeader) {
        this.context = context;
        this.dataHeader = dataHeader;
        this.generics = generics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_tawaran_rekomendasi, parent, false);
            return new HeaderViewHolder (v);
        }
        else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_tawaran, parent, false);
            return new GenericViewHolder (v);
        }
        return null;
    }

    private Tawaran getItem (int position) {
        return generics.get (position);
    }


    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;


        } else if(holder instanceof GenericViewHolder) {
            final Tawaran item = getItem(position - 1);


        }
    }

    //    need to override this method
    @Override
    public int getItemViewType (int position) {
        if(isPositionHeader (position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }


    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    private boolean isPositionFooter (int position) {
        return position == generics.size () + 1;
    }

    @Override
    public int getItemCount () {
        return generics.size () + 1;
    } //dengan footer + 2


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtuserCount)
        TextView txtuserCount;
        @BindView(R.id.txtToko)
        TextView txtToko;
        @BindView(R.id.txtTandai)
        TextView txtTandai;
        @BindView(R.id.txtRecommended)
        TextView txtRecommended;
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.btnPilih)
        Button btnPilih;
        @BindView(R.id.card_view)
        CardView card_view;
        @BindView(R.id.main_content)
        RelativeLayout main_content;


        public HeaderViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtuserCount)
        TextView txtuserCount;
        @BindView(R.id.txtToko)
        TextView txtToko;
        @BindView(R.id.txtTandai)
        TextView txtTandai;
        @BindView(R.id.txtRecommended)
        TextView txtRecommended;
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.btnPilih)
        Button btnPilih;
        @BindView(R.id.card_view)
        CardView card_view;
        @BindView(R.id.main_content)
        RelativeLayout main_content;

        public GenericViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

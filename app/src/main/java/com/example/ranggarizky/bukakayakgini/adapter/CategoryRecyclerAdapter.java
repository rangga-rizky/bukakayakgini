package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.CategoryActivity;
import com.example.ranggarizky.bukakayakgini.DetailRequestKuActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.fragment.ParentCategoryFragment;
import com.example.ranggarizky.bukakayakgini.model.Kategori;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ranggarizky on 5/11/2016.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {
    OnItemClickListener mItemClickListener;
    private List<Kategori> datalist;
    private Context context;
    private ParentCategoryFragment.onKategoriSelected listener;
   // private Tracking item;
    int mode;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.txtKategori)
        TextView txtKategori;
        @BindView(R.id.imgArrow)
        ImageView imgArrow;
        @BindView(R.id.layout)
        LinearLayout layout;

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



    public CategoryRecyclerAdapter(Context context, List<Kategori> datalist,ParentCategoryFragment.onKategoriSelected listener) {
        this.datalist = datalist;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Kategori item = datalist.get(position);
        holder.txtKategori.setText(item.getName());
        if(item.getChildren().size() == 0){
            holder.imgArrow.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getChildren().size() > 0){
                    listener.onKategoriSelected(item.getName(),item.getChildren());
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("nama_kategori",item.getName());
                    intent.putExtra("id_kategori",item.getId());
                    ((CategoryActivity) context).setResult(1,intent);
                    ((CategoryActivity)context).finish();
                }
            }
        });



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


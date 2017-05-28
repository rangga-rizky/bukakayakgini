package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.fragment.BarangkuListFragment;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class BarangBukaLapakRecyclerAdapter extends RecyclerView.Adapter<BarangBukaLapakRecyclerAdapter.MyViewHolder> {

    private List<BarangBukaLapak> verticalList;
    private List<String> barangDipilih;
    private Context context;
    private BarangkuListFragment fragment;
    private int limit;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBarang)
        ImageView imgBarang;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtStok)
        TextView txtStok;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public BarangBukaLapakRecyclerAdapter(Context context, List<BarangBukaLapak> verticalList, BarangkuListFragment fragment,int limit ){
        this.verticalList = verticalList;
        this.context = context;
        this.limit = limit;
        this.fragment = fragment;
        barangDipilih = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barang_ku, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BarangBukaLapak item = verticalList.get(position);
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

             @Override
             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                 if(isChecked){
                     if(barangDipilih.size() == limit){
                         buttonView.setChecked(false);
                         Toast.makeText(context,"Kamus sudah menawarkan 3 barang pada request ini",Toast.LENGTH_SHORT).show();
                     }else{
                         barangDipilih.add(item.getId());

                     }
                 }else{
                     barangDipilih.remove((Object) item.getId());
                 }
                 fragment.changeJumlah(String.valueOf(barangDipilih.size()));

             }
                                                   }
        );
        holder.txtStok.setText("Stok : "+item.getStock());
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
        holder.txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(item.getPrice())));
        if(item.getName().length() > 25){
            holder.txtTitle.setText(item.getName().substring(0,25)+"...");
        }else{
            holder.txtTitle.setText(item.getName());
        }
        Picasso.with(context)
                .load(item.getSmall_images().get(0))
                .placeholder(R.drawable.dummy)
                .into(holder.imgBarang);

    }

    public List<String> getBarangDipilih() {
        return barangDipilih;
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }



}


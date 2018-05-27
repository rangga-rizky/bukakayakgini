package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.model.Kategori;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseKategori;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ranggarizky on 5/11/2016.
 */
public class CategorySubscribeRecyclerAdapter extends RecyclerView.Adapter<CategorySubscribeRecyclerAdapter.MyViewHolder> {
    OnItemClickListener mItemClickListener;
    private List<Kategori> datalist;
    private Context context;
   // private Tracking item;
    int mode;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.btnRemove)
        ImageView btnRemove;


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

    public CategorySubscribeRecyclerAdapter(Context context, List<Kategori> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kategori_subscribe, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Kategori item = datalist.get(position);
        holder.txtTitle.setText(item.getNama());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSubscribe(item,position);
            }
        });
    }

    private void removeSubscribe(final Kategori item, final int position ){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        SessionManager sessionManager = new SessionManager(context);
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseApi> call = apiService.unsubscribe(sessionManager.getUid(),item.getKategori(),"ant0k");

        //proses call
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(response.body().getSuccess().equals("1")){
                        Toast.makeText(context, "Kategori Berhasil diunsubscribe", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                        datalist.remove(position);
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(context, "Terjadi kesalahan, coba lain kali", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return datalist.size();
    }
}


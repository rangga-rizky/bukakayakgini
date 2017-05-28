package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.RequestJoinActivity;
import com.example.ranggarizky.bukakayakgini.model.Join;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ranggarizky on 5/11/2016.
 */
public class WannaJoinAdapter extends RecyclerView.Adapter<WannaJoinAdapter.MyViewHolder> {
    OnItemClickListener mItemClickListener;
    private List<Join> datalist;
    private Context context;
    private ProgressDialog progressDialog;
   // private Tracking item;
    int mode;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.txtNama)
        TextView txtNama;
        @BindView(R.id.imgAva)
        ImageView imgAva;
        @BindView(R.id.utama)
        RelativeLayout utama;

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



    public WannaJoinAdapter(Context context, List<Join> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog_join, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Join item = datalist.get(position);
        loadUser(item.getId_join(),item.getId_buyer(),holder,item.getPesan());


    }
    private void openDIalog(final String idJoin, String pesan, TextView txtNama, String urlImage) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm_join);
        Button btnAbaikan = (Button) dialog.findViewById(R.id.btnAbaikan);
        ImageView imgAvadialog = (ImageView) dialog.findViewById(R.id.imgAva);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        final TextView txtPesan = (TextView) dialog.findViewById(R.id.txtPesan);
        TextView txtNamaUser = (TextView) dialog.findViewById(R.id.txtNama);
        txtNamaUser.setText(txtNama.getText().toString());
        txtPesan.setText(pesan);
        Picasso.with(context)
                .load(urlImage)
                .placeholder(R.drawable.dummy)
                .into(imgAvadialog);
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateJoin(idJoin, "1");
                dialog.dismiss();
            }
        });
        btnAbaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateJoin(idJoin, "2");
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        dialog.show();
    }

    private void updateJoin(String id_join,String status){
        WEB_API apiService = WEB_API.client.create(WEB_API.class);
        Call<ResponseApi> call = apiService.updateJoin(id_join,status,"ant0k");

        //proses call
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                ResponseApi apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getSuccess().equals("1")){
                        Toast.makeText(context, "update berhasil", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context, "Terjadi kesalahan coba lain kali", Toast.LENGTH_SHORT).show();
                    }
                }

                ((RequestJoinActivity)context).load();
            }


            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                // Log error
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadUser(final String id_join, String id, final MyViewHolder holder, final String pesan){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getUserbyID(id);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, final Response<ResponseObject> response) {
                final ResponseObject responseApi = response.body();
                holder.txtNama.setText(response.body().getUser().getUsername());
                Picasso.with(context)
                        .load(response.body().getUser().getAvatar())
                        .placeholder(R.drawable.dummy)
                        .into(holder.imgAva);
                holder.utama.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDIalog(id_join,pesan,holder.txtNama,responseApi.getUser().getAvatar());
                    }
                });
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


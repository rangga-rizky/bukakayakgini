package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Space;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

/**
 * Created by ranggarizky on 9/12/2016.
 */
public class RequestJoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String urlImage1,urlImage2;
    //private static final int TYPE_FOOTER = 2;

    ArrayList<Join> generics;
    ArrayList<Join> dataHeader;
    String jumlahRequest,jumlahJoin;
    Context context;

    public RequestJoinAdapter(Context context, ArrayList<Join> generics,  ArrayList<Join> dataHeader,String jumlahRequest,String jumlahJoin) {
        this.context = context;
        this.dataHeader = dataHeader;
        this.generics = generics;
        this.jumlahRequest = jumlahRequest;
        this.jumlahJoin = jumlahJoin;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.header_request_join, parent, false);
            return new HeaderViewHolder (v);
        }
        else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_join, parent, false);
            return new GenericViewHolder (v);
        }
        return null;
    }

    private Join getItem (int position) {
        return generics.get (position);
    }


    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.txtJumlahMintaJoin.setText(jumlahRequest+" orang meminta join dalam request ini");
            if(jumlahJoin.equals("0")){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)headerHolder.footer.getLayoutParams();
                params.setMargins(4, 0, 4, 10); //substitute parameters for left, top, right, bottom
                headerHolder.footer.setLayoutParams(params);
                headerHolder.txtJumlahSudahJoin.setText("Belum ada orang yang join dalam request ini");
            }else{
                headerHolder.txtJumlahSudahJoin.setText(jumlahJoin+" orang sudah join dalam request ini");
            }

            if(jumlahRequest.equals("0")){
                headerHolder.card1.setVisibility(View.GONE);
                headerHolder.space.setVisibility(View.GONE);
            }

            headerHolder.data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDIalog(dataHeader.get(0).getId_join(),dataHeader.get(0).getPesan(),headerHolder.txtNama,urlImage1);
                }
            });

            headerHolder.data2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDIalog(dataHeader.get(1).getId_join(),dataHeader.get(1).getPesan(),headerHolder.txtNama2,urlImage2);
                }
            });

            switch (Integer.valueOf(jumlahRequest)){
                case 0 : headerHolder.data.setVisibility(View.GONE);
                        headerHolder.data2.setVisibility(View.GONE);
                        headerHolder.btnLihatSemua.setVisibility(View.GONE);
                        ;break;
                case 1 : headerHolder.data2.setVisibility(View.GONE);
                    loadUserHeader(dataHeader.get(0).getId_buyer(),headerHolder.txtNama,headerHolder.imgAva,1);
                    headerHolder.btnLihatSemua.setVisibility(View.GONE);
                          break;
                case 2 : loadUserHeader(dataHeader.get(0).getId_buyer(),headerHolder.txtNama,headerHolder.imgAva,1);
                    loadUserHeader(dataHeader.get(1).getId_buyer(),headerHolder.txtNama2,headerHolder.imgAva2,2);
                    headerHolder.btnLihatSemua.setVisibility(View.GONE);
                    break;
                default:
                    loadUserHeader(dataHeader.get(0).getId_buyer(),headerHolder.txtNama,headerHolder.imgAva,1);
                    loadUserHeader(dataHeader.get(1).getId_buyer(),headerHolder.txtNama2,headerHolder.imgAva2,2);
                    break;
            }

            headerHolder.btnLihatSemua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_request_join_list);
                    RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.item_list);

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
                    });
                    WannaJoinAdapter mAdapter = new WannaJoinAdapter(context,dataHeader);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                    dialog.show();
                }
            });

        } else if(holder instanceof GenericViewHolder) {
            final Join item = getItem(position - 1);
            loadUser(item.getId_buyer(), (GenericViewHolder) holder);


        }
    }

    private void openDIalog(final String idJoin, String pesan, TextView txtNama, String urlImage){
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
                updateJoin(idJoin,"1");
                dialog.dismiss();
            }
        });
        btnAbaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateJoin(idJoin,"2");
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


    private void loadUserHeader(String id, final TextView txt, final ImageView img, final int dataKe){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getUserbyID(id);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                txt.setText(response.body().getUser().getUsername());
                Picasso.with(context)
                        .load(response.body().getUser().getAvatar())
                        .placeholder(R.drawable.dummy)
                        .into(img);
                if(dataKe == 1){
                    urlImage1 = response.body().getUser().getAvatar();
                }else{
                    urlImage2 = response.body().getUser().getAvatar();
                }
            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

    private void loadUser(final String id, final GenericViewHolder holder){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getUserbyID(id);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                holder.txtNama.setText(response.body().getUser().getUsername());
                Picasso.with(context)
                        .load(response.body().getUser().getAvatar())
                        .placeholder(R.drawable.dummy)
                        .into(holder.imgAva);
            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
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
        TextView txtJumlahMintaJoin,txtJumlahSudahJoin,txtNama,txtNama2;
        RelativeLayout data,data2;
        LinearLayout footer;
        LinearLayout card1;
        Button btnLihatSemua;
        ImageView imgAva2,imgAva;
        Space space;

        public HeaderViewHolder (View itemView) {
            super (itemView);
            this.txtJumlahMintaJoin = (TextView) itemView.findViewById (R.id.txtJumlahMintaJoin);
            this.txtJumlahSudahJoin = (TextView) itemView.findViewById (R.id.txtJumlahSudahJoin);
            this.txtNama = (TextView) itemView.findViewById (R.id.txtNama);
            this.card1 = (LinearLayout) itemView.findViewById (R.id.card1);
            this.footer = (LinearLayout) itemView.findViewById (R.id.footer);
            this.txtNama2 = (TextView) itemView.findViewById (R.id.txtNama2);
            this.imgAva = (ImageView) itemView.findViewById (R.id.imgAva);
            this.imgAva2 = (ImageView) itemView.findViewById (R.id.imgAva2);
            this.data = (RelativeLayout) itemView.findViewById (R.id.data);
            this.data2 = (RelativeLayout) itemView.findViewById (R.id.data2);
            this.space = (Space) itemView.findViewById (R.id.space);
            this.btnLihatSemua = (Button) itemView.findViewById (R.id.btnLihatSemua);
        }
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView txtNama;
        ImageView imgAva;

        public GenericViewHolder (View itemView) {
            super (itemView);
            this.txtNama = (TextView) itemView.findViewById (R.id.txtNama);
            this.imgAva = (ImageView) itemView.findViewById (R.id.imgAva);
        }
    }
}

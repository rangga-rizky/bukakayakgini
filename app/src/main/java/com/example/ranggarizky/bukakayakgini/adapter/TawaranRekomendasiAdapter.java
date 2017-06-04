package com.example.ranggarizky.bukakayakgini.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.example.ranggarizky.bukakayakgini.DetailTawaranActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.RequestJoinActivity;
import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.Join;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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

            if(dataHeader.getStatus().equals("3")){
                headerHolder.txtTandai.setVisibility(View.VISIBLE);
            }
            loadBarangHeader(dataHeader.getId_produk(),headerHolder);
            headerHolder.main_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailTawaranActivity.class);
                    intent.putExtra("id",dataHeader.getId_produk());
                    intent.putExtra("user_id",dataHeader.getDemand().getId_user());
                    intent.putExtra("tawaran_id",dataHeader.getId());
                    intent.putExtra("tawaran_status",dataHeader.getStatus());
                    context.startActivity(intent);
                }
            });

            headerHolder.btnPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_confirm);
                    TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
                    TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
                    Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
                    Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
                    txtJudul.setText("Pilih Tawaran");
                    txtDeskripsi.setText("Apakah Kamu yakin memberli barang ini ?");
                    btnTerima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            pilihBarang(dataHeader);
                        }
                    });
                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });


        } else if(holder instanceof GenericViewHolder) {
            final Tawaran item;
            final GenericViewHolder genericHolder = (GenericViewHolder) holder;
            if(dataHeader == null){
                item = getItem(position);
            }else{
                item = getItem(position - 1);
            }

            if(item.getStatus().equals("3")){
                genericHolder.txtTandai.setVisibility(View.VISIBLE);
            }
            loadBarangGeneric(item.getId_produk(),genericHolder);
            genericHolder.main_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailTawaranActivity.class);
                    intent.putExtra("id",item.getId_produk());
                    intent.putExtra("user_id",item.getDemand().getId_user());
                    intent.putExtra("tawaran_id",item.getId());
                    intent.putExtra("tawaran_status",item.getStatus());
                    context.startActivity(intent);
                }
            });

            genericHolder.btnPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_confirm);
                    TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
                    TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
                    Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
                    Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
                    txtJudul.setText("Pilih Tawaran");
                    txtDeskripsi.setText("Apakah Kamu yakin memberli barang ini ?");
                    btnTerima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            pilihBarang(item);
                        }
                    });
                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    private void pilihBarang(Tawaran item){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        SessionManager sessionManager = new SessionManager(context);
        API apiService = API.client.create(API.class);
        BarangBukaLapak produk = new BarangBukaLapak();
        produk.setId(item.getId_produk());
        produk.setQuantity("1");
        String auth = sessionManager.getUid()+":"+sessionManager.getToken();
        Base64Converter base64Converter = new Base64Converter();
        auth = "basic "+base64Converter.encodeString(auth);
        Call<ResponseObject> call = apiService.addToCart(auth,item.getId_produk()+".json",produk);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                progressDialog.dismiss();
                ResponseObject apiresponse = response.body();
                if (response.body() == null) {
                    Log.e("cok", "no response");
                } else {
                    if(apiresponse.getStatus().equals("OK")){
                        openRedirectDialog();
                        Toast.makeText(context, "Barang telah ditambahkan ke keranjang bukalapak anda", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context, apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Log error
                progressDialog.dismiss();
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openRedirectDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm);
        TextView txtJudul = (TextView) dialog.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = (TextView) dialog.findViewById(R.id.txtDeskripsi);
        Button btnTerima = (Button) dialog.findViewById(R.id.btnTerima);
        Button btnBatal = (Button) dialog.findViewById(R.id.btnBatal);
        btnTerima.setText("OK, Mengerti");
        txtJudul.setText("Piih Tawaran Sukses");
        txtDeskripsi.setText("Kamu akan diarahkan ke halaman keranjang Bukalapak untuk proses transaksinya.");
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bukaKeranjang();
                dialog.dismiss();

            }
        });
        btnBatal.setVisibility(View.GONE);
        dialog.show();
    }

    private void bukaKeranjang(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.bukalapak.com/cart/carts"));
        context.startActivity(i);
    }

    private void loadBarangGeneric(String id, final GenericViewHolder holder){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getProdukbyID(id+".json");

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null){
                    if(response.body().getProduct().getCondition().equals("new")){
                        holder.txtStatus.setText("Baru");
                    }else{
                        holder.txtStatus.setText("Bekas");
                    }
                    holder.txtuserCount.setText("("+response.body().getProduct().getRating().getUser_count()+")");
                    holder.txtToko.setText("oleh "+response.body().getProduct().getSeller_name()+","+response.body().getProduct().getCity());
                    holder.rating.setRating(response.body().getProduct().getRating().getAverage_rate());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    holder.txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(response.body().getProduct().getPrice())));
                    Picasso.with(context)
                            .load(response.body().getProduct().getSmall_images().get(0))
                            .placeholder(R.drawable.dummy)
                            .into(holder.imgBarang);

                }

            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }

    private void loadBarangHeader(String id, final HeaderViewHolder holder){
        API apiService = API.client.create(API.class);
        Call<ResponseObject> call = apiService.getProdukbyID(id+".json");

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null){
                    if(response.body().getProduct().getCondition().equals("new")){
                        holder.txtStatus.setText("Baru");
                    }else{
                        holder.txtStatus.setText("Bekas");
                    }
                    holder.txtuserCount.setText("("+response.body().getProduct().getRating().getUser_count()+")");
                    holder.txtToko.setText("oleh "+response.body().getProduct().getSeller_name()+","+response.body().getProduct().getCity());
                    holder.rating.setRating(response.body().getProduct().getRating().getAverage_rate());
                    NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                    holder.txtHarga.setText("Rp"+rupiahFormat.format(Double.parseDouble(response.body().getProduct().getPrice())));
                    Picasso.with(context)
                            .load(response.body().getProduct().getSmall_images().get(0))
                            .placeholder(R.drawable.dummy)
                            .into(holder.imgBarang);

                }

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
        if(dataHeader == null){
            return TYPE_ITEM;
        }
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
        if(dataHeader == null){
            return generics.size ();
        }
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

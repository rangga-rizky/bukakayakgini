package com.example.ranggarizky.bukakayakgini.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ranggarizky.bukakayakgini.CategoryActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.adapter.CategoryRecyclerAdapter;
import com.example.ranggarizky.bukakayakgini.model.Kategori;
import com.example.ranggarizky.bukakayakgini.model.ResponseCategory;
import com.example.ranggarizky.bukakayakgini.util.API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentCategoryFragment extends Fragment {
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private CategoryRecyclerAdapter adapter;
    private onKategoriSelected mListener;

    public ParentCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_category, container, false);
        ButterKnife.bind(this,view);
        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        loadData();
        return view;
    }

    private void loadData(){
        API apiService = API.client.create(API.class);
        Call<ResponseCategory> call = apiService.getCategories();

        //proses call
        call.enqueue(new Callback<ResponseCategory>() {
            @Override
            public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {

                adapter = new CategoryRecyclerAdapter(getActivity(), response.body().getKategoris(),mListener);
                recyclerView.setAdapter(adapter);

            }


            @Override
            public void onFailure(Call<ResponseCategory> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Failed to Connect Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof onKategoriSelected) {
            mListener = (onKategoriSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    public interface onKategoriSelected {
        void onKategoriSelected(String name,ArrayList<Kategori> listKategori);
    }


    public static ParentCategoryFragment newInstance() {
        return new ParentCategoryFragment();
    }


    public void onResume(){
        super.onResume();
        ((CategoryActivity) getActivity())
                .setActionBarTitle("PILIH KATEGORI");

    }

}

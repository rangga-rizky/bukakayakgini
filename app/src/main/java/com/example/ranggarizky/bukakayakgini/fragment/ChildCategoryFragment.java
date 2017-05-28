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
import com.example.ranggarizky.bukakayakgini.PrepareRequestActivity;
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
public class ChildCategoryFragment extends Fragment {
    private static final String KATEGORI= "kategori";
    private static final String DATA= "data";
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    CategoryRecyclerAdapter adapter;
    private String namaKategori;
    private ArrayList<Kategori> listKategori;
    private ParentCategoryFragment.onKategoriSelected mListener;

    public ChildCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_category, container, false);
        ButterKnife.bind(this,view);
        final Bundle args = getArguments();
        namaKategori = args.getString(KATEGORI);
        listKategori = (ArrayList<Kategori>)  args.getSerializable(DATA);
        LinearLayoutManager mLayoutManagerOrder = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManagerOrder);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);

        adapter = new CategoryRecyclerAdapter(getActivity(), listKategori,mListener);
        recyclerView.setAdapter(adapter);
        return view;
    }



    public static ChildCategoryFragment newInstance( String name,ArrayList<Kategori> listKategori) {
        final Bundle args = new Bundle();
        args.putString(KATEGORI, name);
        args.putSerializable(DATA,listKategori);
        final ChildCategoryFragment fragment = new ChildCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ParentCategoryFragment.onKategoriSelected) {
            mListener = (ParentCategoryFragment.onKategoriSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    public void onResume(){
        super.onResume();
        ((CategoryActivity) getActivity())
                .setActionBarTitle(namaKategori.toUpperCase());

    }

}

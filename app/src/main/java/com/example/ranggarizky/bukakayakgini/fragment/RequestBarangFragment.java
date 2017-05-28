package com.example.ranggarizky.bukakayakgini.fragment;



import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ranggarizky.bukakayakgini.PrepareRequestActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.example.ranggarizky.bukakayakgini.util.Validation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestBarangFragment extends Fragment {
    @BindView(R.id.editBarang)
    EditText editBarang;
    private onRequestBarang mListener;

    public RequestBarangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_barang, container, false);
        ButterKnife.bind(this,view);
        editBarang.getBackground().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_IN);
        return view;
    }


    public static RequestBarangFragment newInstance() {
        return new RequestBarangFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof onRequestBarang) {
            mListener = (onRequestBarang) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    public interface onRequestBarang {
        void onRequestBarang(String barang);
    }

    @OnClick(R.id.btnRequest)
    public void cariBarang(View view){
        if(Validation.checkEmpty(editBarang)){
            mListener.onRequestBarang(editBarang.getText().toString());
        }
    }



    public void onResume(){
        super.onResume();
        ((PrepareRequestActivity) getActivity())
                .setActionBarTitle("REQUEST BARANG");

    }

}

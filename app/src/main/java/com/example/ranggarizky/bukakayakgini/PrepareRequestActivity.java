package com.example.ranggarizky.bukakayakgini;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ranggarizky.bukakayakgini.fragment.BeforeRequestFragment;
import com.example.ranggarizky.bukakayakgini.fragment.RequestBarangFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrepareRequestActivity extends AppCompatActivity  implements RequestBarangFragment.onRequestBarang{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_request);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, RequestBarangFragment.newInstance(), "requestBarang")
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestBarang(String barang) {
        final BeforeRequestFragment beforeRequestFragment =
                BeforeRequestFragment.newInstance(barang);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left )
                .replace(R.id.root_layout, beforeRequestFragment, "beforeRequestFragment")
                .addToBackStack(null)
                .commit();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1 && requestCode == 1){
            finish();
        }
    }
}

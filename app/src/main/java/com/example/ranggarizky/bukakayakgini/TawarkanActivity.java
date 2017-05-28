package com.example.ranggarizky.bukakayakgini;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.ranggarizky.bukakayakgini.fragment.BarangkuListFragment;
import com.example.ranggarizky.bukakayakgini.fragment.RequestBarangFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TawarkanActivity extends AppCompatActivity implements BarangkuListFragment.onPilihBarang {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String id_permintaan;
    private  int jumlahTawaranku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tawarkan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id_permintaan = getIntent().getStringExtra("id_permintaan");
        jumlahTawaranku = getIntent().getIntExtra("jumlahTawaranku",0);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, BarangkuListFragment.newInstance(id_permintaan,jumlahTawaranku), "pilihBarang")
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

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onPilihBarang(String id_barang) {

    }
}

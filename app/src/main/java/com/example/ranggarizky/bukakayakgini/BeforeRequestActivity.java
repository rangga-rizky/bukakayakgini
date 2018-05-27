package com.example.ranggarizky.bukakayakgini;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ranggarizky.bukakayakgini.fragment.BeforeRequestFragment;
import com.example.ranggarizky.bukakayakgini.fragment.RequestBarangFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeforeRequestActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String namaBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_request);

        namaBarang = getIntent().getStringExtra("nama");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, BeforeRequestFragment.newInstance(namaBarang), "BeforerequestBarang")
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(namaBarang);
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
}

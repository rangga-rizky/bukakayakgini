package com.example.ranggarizky.bukakayakgini;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ranggarizky.bukakayakgini.fragment.BeforeRequestFragment;
import com.example.ranggarizky.bukakayakgini.fragment.ChildCategoryFragment;
import com.example.ranggarizky.bukakayakgini.fragment.ParentCategoryFragment;
import com.example.ranggarizky.bukakayakgini.fragment.RequestBarangFragment;
import com.example.ranggarizky.bukakayakgini.model.Kategori;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity implements ParentCategoryFragment.onKategoriSelected{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, ParentCategoryFragment.newInstance(), "category")
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
    public void onKategoriSelected(String name, ArrayList<Kategori> listKategori) {
        final ChildCategoryFragment beforeRequestFragment =
                ChildCategoryFragment.newInstance(name,listKategori);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left )
                .replace(R.id.root_layout, beforeRequestFragment, "childkategori")
                .addToBackStack(null)
                .commit();
    }
}

package com.example.ranggarizky.bukakayakgini;


import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ranggarizky.bukakayakgini.fragment.HomeFragment;
import com.example.ranggarizky.bukakayakgini.fragment.MenawarkanBarangFragment;
import com.example.ranggarizky.bukakayakgini.fragment.ProfileFragment;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.util.API;
import com.example.ranggarizky.bukakayakgini.util.Base64Converter;
import com.example.ranggarizky.bukakayakgini.util.SessionManager;
import com.example.ranggarizky.bukakayakgini.util.WEB_API;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    TextView txtNameHeader,txtEmail;
    ImageView imageAva;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        sessionManager = new SessionManager(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        loadUser();
        txtNameHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtNameHeader);
        txtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        imageAva = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageAva);
        txtNameHeader.setText(sessionManager.getUsername());
        txtEmail.setText(sessionManager.getEmail());
        navigationView.setNavigationItemSelectedListener(this);
        setupFragment();
    }

    private void loadUser(){
        API apiService = API.client.create(API.class);
        String auth = sessionManager.getUid()+":"+sessionManager.getToken();
        Base64Converter base64Converter = new Base64Converter();
        auth = "basic "+base64Converter.encodeString(auth);
        Call<ResponseObject> call = apiService.getUser(auth);

        //proses call
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if(txtNameHeader!=null && imageAva!=null){
                        if(response.body()!= null){
                            txtNameHeader.setText(response.body().getUser().getName());
                            txtEmail.setText(response.body().getUser().getEmail());
                            sessionManager.setUsername(response.body().getUser().getName());
                            sessionManager.setEmail(response.body().getUser().getEmail());

                            sessionManager.setLokasi(response.body().getUser().getAddress().getCity());
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getUser().getAvatar())
                                    .placeholder(R.drawable.dummy)
                                    .into(imageAva);
                        }

                    }

            }


            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("cok", "onFailure: ", t.fillInStackTrace());
            }
        });
    }


    private void setupFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        invalidateOptionsMenu();
         if (id == R.id.nav_request_lain) {
            fragment = new MenawarkanBarangFragment();
        }
        else if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        }
        else if (id == R.id.nav_bantuan) {
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("url","http://bl.rendrahutama.my.id/public/panduan/");
            intent.putExtra("title","PUSAT BANTUAN");
            startActivity(intent);
        }
        else if (id == R.id.nav_bukakayakgini) {
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("url","http://bl.rendrahutama.my.id/public/about/");
            intent.putExtra("title","TENTANG BUKAKAYAKGINI");
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        }
        else if (id == R.id.nav_aplikasi) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_version_app);
            dialog.show();

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public Toolbar getToolbar(){
       return toolbar;
    }
}

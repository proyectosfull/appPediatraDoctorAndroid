package com.diazmiranda.juanjose.pediatra.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.diazmiranda.juanjose.pediatra.Fragments.DescriptionMedicalConsultationsMade;
import com.diazmiranda.juanjose.pediatra.Fragments.DescriptionMedicalConsultationsPending;
import com.diazmiranda.juanjose.pediatra.Fragments.MedicalConsultationsMade;
import com.diazmiranda.juanjose.pediatra.Fragments.MedicalConsultationsPending;
import com.diazmiranda.juanjose.pediatra.Fragments.Payments;
import com.diazmiranda.juanjose.pediatra.Fragments.ProfessionalProfile;
import com.diazmiranda.juanjose.pediatra.Fragments.ResponseNull;
import com.diazmiranda.juanjose.pediatra.Fragments.Start;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.UI;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfessionalProfile.OnFragmentInteractionListener, MedicalConsultationsPending.OnFragmentInteractionListener,
        MedicalConsultationsMade.OnFragmentInteractionListener, Payments.OnFragmentInteractionListener, ResponseNull.OnFragmentInteractionListener, Start.OnFragmentInteractionListener,
        DescriptionMedicalConsultationsPending.OnFragmentInteractionListener, DescriptionMedicalConsultationsMade.OnFragmentInteractionListener {

    private final static String TAG = "MainActivity";
    private SharedPreferencesHelper sharedPreferences;
    private ProgressDialog progressDialog;

    public DrawerLayout drawer;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = new SharedPreferencesHelper(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        setTitle(R.string.nav_inicio);
        Start fragment = new Start();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Vista, fragment, "Start");
        fragmentTransaction.commit();

        putPersonalData();
    }

    public void putPersonalData() {
        TextView txtName = navigationView.getHeaderView(0).findViewById(R.id.idNombre);
        TextView txtEmail = navigationView.getHeaderView(0).findViewById(R.id.idEmail);
        RoundedImageView imagePerfilView = navigationView.getHeaderView(0).findViewById(R.id.perfilImage);

        txtName.setText(sharedPreferences.getNombre());
        txtEmail.setText(sharedPreferences.getCorreo());

        if (sharedPreferences.getPerfilImage() != null){
            imagePerfilView.setImageBitmap(StringToBitMap(sharedPreferences.getPerfilImage()));
        }
    }

    public void actualizarNombre() {
        TextView txtName = navigationView.getHeaderView(0).findViewById(R.id.idNombre);
        txtName.setText(sharedPreferences.getNombre());
    }

    public void actualizarPerfilImage() {
        RoundedImageView imagePerfilView = navigationView.getHeaderView(0).findViewById(R.id.perfilImage);
        imagePerfilView.setImageBitmap(StringToBitMap(sharedPreferences.getPerfilImage()));
    }


    public void actualizarPayments(String list){
        Payments pagos = new Payments();

        SharedPreferencesHelper sharedPreferences;
        sharedPreferences = new SharedPreferencesHelper(MainActivity.this);
        pagos.UpdateRecyclerView();
    }

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            setTitle(R.string.nav_inicio);
            Start fragment = new Start();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Vista, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_perfil) {
            setTitle(R.string.nav_perfil);
            ProfessionalProfile fragment = new ProfessionalProfile();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Vista, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_citasPendientes) {
            setTitle(R.string.nav_citas_pendientes);
            MedicalConsultationsPending fragment = new MedicalConsultationsPending();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Vista, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_citasRealizadas) {
            setTitle(R.string.nav_citas_realizadas);
            MedicalConsultationsMade fragment = new MedicalConsultationsMade();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Vista, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_pagos) {
            setTitle(R.string.nav_pagos_recibidos);
            Payments fragment = new Payments();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Vista, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_cerrar_sesion) {
            enviarDatos();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void enviarDatos() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.logout();
        progressDialog = UI.showWaitDialog(MainActivity.this);

        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(MainActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                    progressDialog.dismiss();
                    sharedPreferences.removeToken();
                    sharedPreferences.removeFCMToken();
                    sharedPreferences.removeMediaToken();
                    sharedPreferences.removeMediaTokenExp();
                    sharedPreferences.removeIdDependiente();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                progressDialog.dismiss();
            }
        });
    }
}

package com.diazmiranda.juanjose.pediatra.Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShowUniqueImage extends AppCompatActivity {

    private SharedPreferencesHelper sharedPreferences;
    private RelativeLayout linearLayout;
    private ImageView imgeView;
    private Button OkButton;

    private RequestQueue queue;
    private ScaleGestureDetector scaleGestureDetector;
    private float xBegin = 0;
    private float yBegin = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_img_description_medical_consultations);
        sharedPreferences = new SharedPreferencesHelper(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbarImg);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white),
                PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences.removeNameImage();
                sharedPreferences.removeIdConsulta();
                sharedPreferences.removeIdDependiente();
                sharedPreferences.removeMediaTokenExp();
                sharedPreferences.removeMediaToken();

                finish();
            }
        });

        if (getSupportActionBar() != null) {
            if (getSupportActionBar().isShowing()) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }

        imgeView = findViewById(R.id.imgDetalleConsultaZoom);
        OkButton = findViewById(R.id.OkButton);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();

        Boolean response = bundle.getBoolean("ConfirmationVisibility");
        final String list = bundle.getString("data");
        Log.e("DATA", list);

        if (response){
            OkButton.setVisibility(View.VISIBLE);
        }else{
            OkButton.setVisibility(View.INVISIBLE);
        }

        linearLayout = findViewById(R.id.linerLayoutImgDetalleConsulta);

        linearLayout.setBackgroundColor(Color.parseColor("#000000"));
        imgSintomas();
        xBegin = imgeView.getScaleX();
        yBegin = imgeView.getScrollY();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener(imgeView));

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePaymentStatus(sharedPreferences.getidConsulta());
                finish();
            }
        });

    }

    private void ChangePaymentStatus(int getidConsulta) {
        String url = "http://23.82.16.144/HDK/api/consulta/pago/" + getidConsulta;
        Log.e("URL", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ACUALIZACIÓN_CORRECTA", response.toString());
                        Toast.makeText(getApplicationContext(), "Pago aprovado", Toast.LENGTH_LONG).show();

                        //UPDATE RECYCLERVIEW
                        Log.e("ITEM", Integer.toString(sharedPreferences.getidImtem()));
                        sharedPreferences.putUpdatePayment(true);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.d("Response", String.valueOf(error));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-type", "application/json; charset=utf-8");
                params.put("Authorization", "bearer " + sharedPreferences.getToken());
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("ToolBar", "Atrás!");
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void imgSintomas() {
        String url = "http://23.82.16.144/HDK/api/multimedia/consulta/" +
                sharedPreferences.getIdDependiente() + "/" +
                sharedPreferences.getMediaTokenExp() + "/" +
                sharedPreferences.getMediaToken() + "/" +
                sharedPreferences.getNameImage().replaceAll("\"", "");

        Picasso.with(getApplicationContext())
                .load(url)
                .resize(800, 8000)
                .centerInside()
                .into(imgeView);
    }

    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
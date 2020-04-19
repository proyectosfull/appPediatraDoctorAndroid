package com.diazmiranda.juanjose.pediatra.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferencesHelper sharedPreferences;
    private TextInputLayout txl_correo;
    private TextInputLayout txl_password;
    private EditText edtCorreo;
    private EditText edtPassword;
    private TextView txvRegistrar;
    private Button btnLogin;
    private RequestQueue queue;
    private String correo;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = new SharedPreferencesHelper(this);
        Util.TOKEN = sharedPreferences.getToken();
        if (Util.TOKEN != null) {
            ValidateToken();
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login);

        startComponents();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                sharedPreferences.putFCMToken(deviceToken);
            }
        });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        startListeners();
        TextChangetListener();
    }

    /**
     * Descripción del método
     *
     * @return
     */
    private void ValidateToken() {

        // Get "payload" of the token to later know if it is still valid
        String token = sharedPreferences.getToken();
        String[] parts = token.split("\\.");
        String payload = parts[1]; // payload
        Log.e("PAYLOAD_CODIFICADO", payload);

        //Decoding Base64 String to UTF-8
        byte[] PayloadDec = Base64.decode(payload, Base64.DEFAULT);
        String PayloadDecString = null;
        try {
            PayloadDecString = new String(PayloadDec, "UTF-8");
            Log.e("PAYLOAD_DECODIFICADO", PayloadDecString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Convert PayloadDecString to JSONObject
        JSONObject PayloadToken = null;
        try {
            PayloadToken = new JSONObject(PayloadDecString);
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }

        //Get exp inside the json and compare if the token is still active
        try {
            if (System.currentTimeMillis() > PayloadToken.getLong("exp")) {

                Log.e("TOKEN_NO_EXPIRADO", "Iniciar MAIN_ACTIVITY");
                goToMain();

            } else {

                Log.e("TOKEN_EXPIRADO", "Iniciar Alert_Dialog");

                AlertDialog.Builder LogIn = new AlertDialog.Builder(this);
                LogIn.setTitle("Actualización de información");
                LogIn.setMessage("Hola " + sharedPreferences.getNombre() + "\n\nLamentamos mucho ser inoportunos, sin embargo para que usted siga utilizando la aplicación sin ningún inconveniente debe volver a iniciar sesión. \n\nLo anterior sóla para actualizar información. ");
                LogIn.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        return;
                    }
                });

                AlertDialog Start = LogIn.create();
                Start.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startComponents() {
        edtCorreo = findViewById(R.id.txt_correo);
        edtPassword = findViewById(R.id.txt_password);
        txvRegistrar = findViewById(R.id.txv_registrar);
        txl_correo = findViewById(R.id.txl_correo);
        txl_password = findViewById(R.id.txl_password);
        btnLogin = findViewById(R.id.btn_login);
        queue = Volley.newRequestQueue(this);
    }

    private void startListeners() {
        txvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtCorreo.getText()) || TextUtils.isEmpty(edtPassword.getText())) {
                    if (TextUtils.isEmpty(edtCorreo.getText())) {
                        txl_correo.setErrorEnabled(true);
                        txl_correo.setError("Por favor, ingrese su correo eléctronico");
                    } else if (TextUtils.isEmpty(edtPassword.getText())) {
                        txl_password.setErrorEnabled(true);
                        txl_password.setError("Por favor, ingrese su contraseña");
                    }
                } else {
                    login();
                }
            }
        });
    }

    private void login() {
        String url = "http://23.82.16.144:8080/HDK/api/auth/pediatra/login";
        correo = edtCorreo.getText().toString();
        password = edtPassword.getText().toString();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("correo", correo);
            jsonParams.put("password", password);
            jsonParams.put("FCMToken", sharedPreferences.getFCMToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("JSON_INICIAR_SESION", jsonParams.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject Obj = response;
                            Boolean OK = Obj.getBoolean("OK");

                            if (OK) {
                                //Guardar TOKEN
                                sharedPreferences.putToken(Obj.getJSONObject("data").getString("token"));
                                Util.TOKEN = Obj.getJSONObject("data").getString("token");

                                //Guardar MEDIA_TOKEN
                                sharedPreferences.putMediaToken(Obj.getJSONObject("data").getJSONObject("media_token").getString("token"));
                                Util.MEDIA_TOKEN = Obj.getJSONObject("data").getJSONObject("media_token").getString("token");

                                //Guardar MEDIA_TOKEN_EXP
                                sharedPreferences.putMediaTokenExp(Obj.getJSONObject("data").getJSONObject("media_token").getString("exp"));
                                Util.MEDIA_TOKEN_EXP = Obj.getJSONObject("data").getJSONObject("media_token").getString("exp");

                                sharedPreferences.putPersonalData(Obj.getJSONObject("data").getJSONObject("pediatra").getString("nombreCompleto"),
                                        Obj.getJSONObject("data").getJSONObject("pediatra").getString("correo"));
                                Util.NOMBRE = Obj.getJSONObject("data").getJSONObject("pediatra").getString("nombreCompleto");
                                Util.CORREO = Obj.getJSONObject("data").getJSONObject("pediatra").getString("correo");

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), Obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Log.e("JSON_INICIAR_SESION", Obj.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        });
        queue.add(request);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void TextChangetListener() {
        edtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCorreo.setError(null);
                txl_correo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtPassword.setError(null);
                txl_password.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}

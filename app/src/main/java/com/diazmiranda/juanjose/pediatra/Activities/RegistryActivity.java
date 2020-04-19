package com.diazmiranda.juanjose.pediatra.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.diazmiranda.juanjose.pediatra.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistryActivity extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtApellidos;
    private EditText edtCedula;
    private EditText edtCedulaEsp;
    private EditText edtCmcp;
    private EditText edtCorreo;
    private EditText edtLugarEstudios;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtCurp;
    private Button btnRegistrar;

    //Volley
    private RequestQueue queue;
    private String nombre;
    private String apellidos;
    private String correo;
    private String lugarEstudios;
    private String password;
    private String cedula;
    private String cedulaEspecialidad;
    private String cmcp;
    private String curp;

    //Validations
    private TextInputLayout nombre_aye;
    private TextInputLayout apellidos_aye;
    private TextInputLayout cedula_aye;
    private TextInputLayout cedulaEsp_aye;
    private TextInputLayout cmcp_aye;
    private TextInputLayout correo_aye;
    private TextInputLayout lugarEstudios_aye;
    private TextInputLayout curp_aye;
    private TextInputLayout passwor_aye;
    private TextInputLayout repassword_aye;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_registry);

        startComponents();
        startListeners();
        TextChangetListener();
    }

    private void startComponents() {
        edtNombre = findViewById(R.id.txt_nombre);
        nombre_aye = findViewById(R.id.txl_nombre);

        edtApellidos = findViewById(R.id.txt_apellidos);
        apellidos_aye = findViewById(R.id.txl_apellidos);

        edtCedula = findViewById(R.id.txt_cedula);
        cedula_aye = findViewById(R.id.txl_cedula);

        edtCedulaEsp = findViewById(R.id.txt_cedula_especialidad);
        cedulaEsp_aye = findViewById(R.id.txl_cedula_especialidad);

        edtCmcp = findViewById(R.id.txt_cmcp);
        cmcp_aye = findViewById(R.id.txl_cmcp);

        edtCorreo = findViewById(R.id.txt_correor);
        correo_aye = findViewById(R.id.txl_correo);

        edtLugarEstudios = findViewById(R.id.txt_LugarDeAtencion);
        lugarEstudios_aye = findViewById(R.id.txl_LugarDeAtencion);

        edtPassword = findViewById(R.id.txt_passwordr);
        passwor_aye = findViewById(R.id.txl_passwordr);

        edtRePassword = findViewById(R.id.txt_repassword);
        repassword_aye = findViewById(R.id.txl_repassword);

        edtCurp = findViewById(R.id.txt_curp);
        curp_aye = findViewById(R.id.txl_curp);

        btnRegistrar = findViewById(R.id.btn_registrar);

        queue = Volley.newRequestQueue(this);
    }

    private void startListeners() {

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtNombre.getText()) ||
                        TextUtils.isEmpty(edtApellidos.getText()) ||
                        TextUtils.isEmpty(edtCedula.getText()) ||
                        TextUtils.isEmpty(edtCorreo.getText()) ||
                        TextUtils.isEmpty(edtLugarEstudios.getText()) ||
                        TextUtils.isEmpty(edtPassword.getText()) ||
                        TextUtils.isEmpty(edtRePassword.getText()) ||
                        TextUtils.isEmpty(edtCurp.getText())) {

                    if (TextUtils.isEmpty(edtNombre.getText())) {
                        nombre_aye.setErrorEnabled(true);
                        nombre_aye.setError("Por favor, ingrese su nombre completo");
                    } else if (TextUtils.isEmpty(edtApellidos.getText())) {
                        apellidos_aye.setErrorEnabled(true);
                        apellidos_aye.setError("Por favor, ingrese sus apellidos");
                    } else if (TextUtils.isEmpty(edtCedula.getText())) {
                        cedula_aye.setErrorEnabled(true);
                        cedula_aye.setError("Por favor, ingrese su cédula profesional");
                    } else if (TextUtils.isEmpty(edtCedulaEsp.getText())) {//dfsdfdsfsdfsdfdsf
                        cedulaEsp_aye.setErrorEnabled(true);
                        cedulaEsp_aye.setError("Por favor, ingrese su cédula de especialidad");
                    } else if (TextUtils.isEmpty(edtCmcp.getText())) {
                        cmcp_aye.setErrorEnabled(true);
                        cmcp_aye.setError("Por favor, ingrese su CMCP");
                    } else if (TextUtils.isEmpty(edtCorreo.getText())) {
                        correo_aye.setErrorEnabled(true);
                        correo_aye.setError("Por favor, ingrese su correo electrónico");
                    } else if (TextUtils.isEmpty(edtPassword.getText())) {
                        passwor_aye.setErrorEnabled(true);
                        passwor_aye.setError("Por favor, ingrese su contraseña");
                    } else if (TextUtils.isEmpty(edtRePassword.getText())) {
                        repassword_aye.setErrorEnabled(true);
                        repassword_aye.setError("Por favor, ingrese su nueva su contraseña");
                    } else if (TextUtils.isEmpty(edtLugarEstudios.getText())) {
                        lugarEstudios_aye.setErrorEnabled(true);
                        lugarEstudios_aye.setError("Por favor, ingrese el nombre del lugar donde atendera a sus pacientes");
                    } else if (TextUtils.isEmpty(edtCurp.getText())) {
                        curp_aye.setErrorEnabled(true);
                        curp_aye.setError("Por favor, seleccione su CURP");
                    }
                } else if (validarEmail(edtCorreo.getText().toString().trim())) {
                    correo_aye.setErrorEnabled(true);
                    correo_aye.setError("El correo electrónico no es válido");
                } else if (!edtPassword.getText().toString().equals(edtRePassword.getText().toString())) {
                    repassword_aye.setErrorEnabled(true);
                    repassword_aye.setError("Las contraseñas no coinciden");
                } else {
                    //Toast.makeText(RegistryActivity.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                    register();
                }
            }
        });
    }

    private void TextChangetListener() {
        //Nombre
        edtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtNombre.setError(null);
                nombre_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Apellidos
        edtApellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtApellidos.setError(null);
                apellidos_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Cédula
        edtCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCedula.setError(null);
                cedula_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Cédula Especialidad
        edtCedulaEsp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCedulaEsp.setError(null);
                cedulaEsp_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //CMCP
        edtCmcp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCmcp.setError(null);
                cmcp_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Correo electrónico
        edtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCorreo.setError(null);
                cedula_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edtLugarEstudios.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtLugarEstudios.setError(null);
                lugarEstudios_aye.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //CURP
        edtCurp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCurp.setError(null);
                curp_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Password
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtPassword.setError(null);
                passwor_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //RePassword
        edtRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtRePassword.setError(null);
                repassword_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public static boolean validarEmail(String email) {

        boolean incorrecto = true;
        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            return false;
        }
        return incorrecto;
    }

    private void register() {
        String url = "http://23.82.16.144:8080/HDK/api/pediatra/nuevo";

        nombre = edtNombre.getText().toString();
        apellidos = edtApellidos.getText().toString();
        correo = edtCorreo.getText().toString();
        lugarEstudios = edtLugarEstudios.getText().toString();
        password = edtPassword.getText().toString();
        cedula = edtCedula.getText().toString();
        cedulaEspecialidad = edtCedulaEsp.getText().toString();
        cmcp = edtCmcp.getText().toString();
        curp =  edtCurp.getText().toString();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nombre", nombre);
            jsonParams.put("apellidos", apellidos);
            jsonParams.put("correo", correo);
            jsonParams.put("lugarEstudios", lugarEstudios);
            jsonParams.put("password", password);
            jsonParams.put("cedula", cedula);
            jsonParams.put("cedulaEspecialidad", cedulaEspecialidad);
            jsonParams.put("cmcp", cmcp);
            jsonParams.put("curp", curp);
        } catch (Exception e) {
            Log.e("Registro", e.getMessage(), e);
            e.printStackTrace();
        }
        Log.i("Registro", "ENTRA");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject Obj = response;
                            Boolean OK = Obj.getBoolean("OK");

                            if (OK) {
                                Toast.makeText(getApplicationContext(), "Registro correcto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), Obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("<<<< Error >>>>", error.getMessage().toString());
                error.getMessage();
            }
        });
        queue.add(request);
    }
}

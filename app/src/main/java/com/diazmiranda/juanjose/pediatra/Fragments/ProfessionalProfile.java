package com.diazmiranda.juanjose.pediatra.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.diazmiranda.juanjose.pediatra.Activities.MainActivity;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.Util;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProfessionalProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //TODO: Others elements
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private static String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    //TODO: Elements of view
    private Button selfie_nuevoPersonal;

    private TextInputLayout contraseña_actual_aye;
    private TextInputLayout contraseña_nueva_aye;
    private EditText contraseña_actual;
    private EditText contraseña_nueva;
    private ImageButton password;

    /*private TextInputLayout nombre_nuevoPersonal_aye;
    private TextInputLayout apellido_nuevoPersonal_aye;
    private TextInputLayout especialidad_nuevoPersonal_aye;
    private TextInputLayout cedula_nuevoPersonal_aye;
    private EditText nombre_nuevoPersonal;
    private EditText apellido_nevoPersonal;
    private EditText especialidad_nuevoPersonal;
    private EditText cedula_nuevoPersonal;*/
    private ImageView nuevoPersonal;
    private ImageButton personal;

    //private TextInputLayout lugatdeAtencion_datos_aye;
    private TextInputLayout costo_datos_aye;
    //private EditText lugardeAtencion_datos;
    private EditText costo_datos;
    private ImageButton datos;

    private ImageView ImageViewSingDoctor;
    private Button ButtonSingDoctor;

    public MainActivity mainActivity;

    public Bitmap bitmap;
    public String Imagen;

    private SharedPreferencesHelper sharedPreferences;
    private final static String TAG = "ProfessionalProfile";

    public ProfessionalProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View view = inflater.inflate(R.layout.f_professional_profile, container, false);
        sharedPreferences = new SharedPreferencesHelper(getActivity().getApplicationContext());

        startComponents(view);
        PutImageSing();
        selectImage();
        ButtonToast();
        TextChangetListener();

        return view;
    }

    public void PutImageSing() {

        if (StringToBitMap(sharedPreferences.getNameImageSing()) != null){
            ImageViewSingDoctor.setImageBitmap(StringToBitMap(sharedPreferences.getNameImageSing()));
        }
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

    @SuppressLint("CutPasteId")
    private void startComponents(View view) {
        //Contraseña
        password = (ImageButton) view.findViewById(R.id.edit_password);
        contraseña_actual = (EditText) view.findViewById(R.id.contraseña_actual);
        contraseña_nueva = (EditText) view.findViewById(R.id.contraseña_nueva);

        contraseña_actual_aye = view.findViewById(R.id.contraseña_actual_aye);
        contraseña_nueva_aye = view.findViewById(R.id.contraseña_nueva_aye);

        //DatosPersonales
        personal = (ImageButton) view.findViewById(R.id.add_personal);
        /*nombre_nuevoPersonal = (EditText) view.findViewById(R.id.nombre_nuevoPersonal);
        apellido_nevoPersonal = (EditText) view.findViewById(R.id.apellido_nevoPersonal);
        especialidad_nuevoPersonal = (EditText) view.findViewById(R.id.especialidad_nuevoPersonal);
        cedula_nuevoPersonal = (EditText) view.findViewById(R.id.cedula_nuevoPersonal);*/

        selfie_nuevoPersonal = (Button) view.findViewById(R.id.selfie_nuevoPersonal);
        nuevoPersonal = (ImageView) view.findViewById(R.id.nuevo_personal);

        /*nombre_nuevoPersonal_aye = view.findViewById(R.id.nombre_nuevoPersonal_aye);
        apellido_nuevoPersonal_aye = view.findViewById(R.id.apellido_nevoPersonal_aye);
        especialidad_nuevoPersonal_aye = view.findViewById(R.id.especialidad_nuevoPersonal_aye);
        cedula_nuevoPersonal_aye = view.findViewById(R.id.cedula_nuevoPersonal_aye);*/


        //Datos de citas
        datos = (ImageButton) view.findViewById(R.id.edit_datos);
        //lugardeAtencion_datos = (EditText) view.findViewById(R.id.lugardeAtencion_datos);
        costo_datos = (EditText) view.findViewById(R.id.costo_datos);

        //lugatdeAtencion_datos_aye = view.findViewById(R.id.lugardeAtencion_datos_aye);
        costo_datos_aye = view.findViewById(R.id.costo_datos_aye);

        ImageViewSingDoctor = (ImageView) view.findViewById(R.id.Img_Sing_Doctor);
        ButtonSingDoctor = (Button) view.findViewById(R.id.Button_Sing_doctor);

    }

    private void ButtonToast() {
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(contraseña_actual.getText()) || TextUtils.isEmpty(contraseña_nueva.getText())) {
                    if (TextUtils.isEmpty(contraseña_actual.getText())) {
                        contraseña_actual_aye.setErrorEnabled(true);
                        contraseña_actual_aye.setError("Por favor, ingrese su contraseña actual");
                    } else if (TextUtils.isEmpty(contraseña_nueva.getText())) {
                        contraseña_nueva_aye.setErrorEnabled(true);
                        contraseña_nueva_aye.setError("Por favor, ingrese su nueva contraseña");
                    }
                } else {
                    try {
                        JSONObject datos = new JSONObject();
                        datos.put("current_password", contraseña_actual.getText().toString());
                        datos.put("new_password", contraseña_nueva.getText().toString());
                        CambiarContraseña(datos.toString());
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });

        /*personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nombre_nuevoPersonal.getText()) ||
                        TextUtils.isEmpty(apellido_nevoPersonal.getText()) ||
                        TextUtils.isEmpty(cedula_nuevoPersonal.getText())) {

                    if (TextUtils.isEmpty(nombre_nuevoPersonal.getText())) {
                        nombre_nuevoPersonal_aye.setErrorEnabled(true);
                        nombre_nuevoPersonal_aye.setError("Por favor, ingrese su nombre");
                    } else if (TextUtils.isEmpty(apellido_nevoPersonal.getText())) {
                        apellido_nuevoPersonal_aye.setErrorEnabled(true);
                        apellido_nuevoPersonal_aye.setError("Por favor, ingrese sus apellidos");
                    } else if (TextUtils.isEmpty(cedula_nuevoPersonal.getText())) {
                        cedula_nuevoPersonal_aye.setErrorEnabled(true);
                        cedula_nuevoPersonal_aye.setError("Por favor, ingrese su cédula");
                    }
                } else {
                    try {
                        JSONObject datos = new JSONObject();
                        datos.put("nombre", nombre_nuevoPersonal.getText().toString());
                        datos.put("apellidos", apellido_nevoPersonal.getText().toString());
                        datos.put("cedula", cedula_nuevoPersonal.getText().toString());
                        cambiarDatosPersonales(datos.toString());
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });*/


        datos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (/*TextUtils.isEmpty(lugardeAtencion_datos.getText()) || */TextUtils.isEmpty(costo_datos.getText())) {
                   /* if (TextUtils.isEmpty(lugardeAtencion_datos.getText())) {
                        lugatdeAtencion_datos_aye.setErrorEnabled(true);
                        lugatdeAtencion_datos_aye.setError("Por favor, ingrese el lugar de atención");
                    } else*/ if (TextUtils.isEmpty(costo_datos.getText())) {
                        costo_datos_aye.setErrorEnabled(true);
                        costo_datos_aye.setError("Por favor, ingrese el costo de la consulta");
                    }
                } else {
                    costo_datos_aye.setErrorEnabled(false);
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("costo", costo_datos.getText().toString());
                        actualizarCostoConsulta(obj.toString());
                    } catch(JSONException e) {

                    }

                }
            }
        });
    }

    public void selectImage() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        selfie_nuevoPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences.putIdImage_P_S(1);
                Util.IMAGE_P_S = 1;

                String ID_IMAGE = Integer.toString(sharedPreferences.getidImage_P_S());
                Log.e("ID_IMAGE", ID_IMAGE);

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {

                    } else {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }

                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Elige una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {
                            openCamera();
                        } else if (options[seleccion] == "Elegir de galeria") {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        } else if (options[seleccion] == "Cancelar") {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        ButtonSingDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences.putIdImage_P_S(2);
                Util.IMAGE_P_S = 2;

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {

                    } else {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }

                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Elige una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {
                            openCamera();
                        } else if (options[seleccion] == "Elegir de galeria") {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        } else if (options[seleccion] == "Cancelar") {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri path;
        Log.e("Preceso: ", "In onActivityResult");

        switch (requestCode) {
            case PHOTO_CODE:
                Log.e("Preceso: ", "In onActivityResult - PHOTO_CODE");
                if (resultCode == RESULT_OK) {
                    // Ruta donde se va a guardar la foto
                    String dir = Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    bitmap = (Bitmap) data.getExtras().get("data");

                    Log.e("Preceso: ", "In onActivityResult - RESULT OK");
                    String IdImage = Integer.toString(sharedPreferences.getidImage_P_S());

                    Log.e("ID_IMAGE", IdImage);

                    if (sharedPreferences.getidImage_P_S() == 1) {
                        Log.e("Preceso: ", "In onActivityResult - PHOTO_CODE - New Personal");
                        nuevoPersonal.setImageBitmap(bitmap);
                        Imagen = getStringImagen(bitmap);
                        ConstruirSolicitud(Imagen);
                    } else if (sharedPreferences.getidImage_P_S() == 2) {
                        Log.e("Preceso: ", "In onActivityResult - PHOTO_CODE - New Sing");

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
                        ImageViewSingDoctor.setLayoutParams(layoutParams);

                        ImageViewSingDoctor.setImageBitmap(bitmap);
                        Imagen = getStringImagen(bitmap);

                        sharedPreferences.putNameImageSing(Imagen);
                        Util.NAME_IMAGE_SING = Imagen;

                        ConstruirSolicitud(Imagen);
                    }
                }
                break;
            case SELECT_PICTURE:
                Log.e("Preceso: ", "In onActivityResult - SELECT_PICTURE");
                if (resultCode == RESULT_OK) {
                    path = data.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);

                        if (sharedPreferences.getidImage_P_S() == 1) {
                            Log.e("Preceso: ", "In onActivityResult - SELECT_PICTURE - ProfessionalProfile Image");
                            nuevoPersonal.setImageBitmap(bitmap);

                            Imagen = getStringImagen(bitmap);
                            ConstruirSolicitud(Imagen);

                        } else if (sharedPreferences.getidImage_P_S() == 2) {
                            Log.e("Preceso: ", "In onActivityResult - SELECT_PICTURE - Sing Image");

                            ImageViewSingDoctor.setImageBitmap(bitmap);

                            sharedPreferences.putNameImageSing(getStringImagen(bitmap));
                            Util.NAME_IMAGE_SING = Imagen;

                            Imagen = getStringImagen(bitmap);
                            ConstruirSolicitud(Imagen);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void openCamera() {
        Log.e("Proceso:", "In camera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PHOTO_CODE);
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // TODO: Rename and change types and number of parameters
    public static ProfessionalProfile newInstance(String param1, String param2) {
        ProfessionalProfile fragment = new ProfessionalProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void CambiarContraseña(final String datos) {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(contraseña_nueva.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(contraseña_actual.getWindowToken(), 0);

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.cambiarPassword(body);


        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getContext(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        LimpiarCamposContraseña();
                        Toast.makeText(getContext(), "¡Contraseña actualizada!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }

    public void actualizarCostoConsulta(final String datos) {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(costo_datos.getWindowToken(), 0);

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.ActualizarCostoConsulta(body);


        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getContext(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        costo_datos.getText().clear();
                        Toast.makeText(getContext(), "Dato actualizado", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }

    @SuppressLint("LongLogTag")
    public void ConstruirSolicitud(String imagen) {
        Log.e("Preceso: ", "In Contruir Solicitud");
        try {
            JSONObject datos = new JSONObject();
            datos.put("type", "image/png");
            datos.put("data", imagen + "\"}");
            Log.e("JSON_MANDAR_IMAGEN", datos.toString().replace("\\", "") + "\"");
            if (sharedPreferences.getidImage_P_S() == 1) {
                Log.e("Preceso: ", "In Contruir Solicitud - New personal");
                SendImagePerfil(datos.toString().replace("\\", ""), imagen);
            } else if (sharedPreferences.getidImage_P_S() == 2) {
                Log.e("Preceso: ", "In Construir solicitud - Sing Image");
                SendImageSing(datos.toString().replace("\\", ""), imagen);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void SendImageSing(final String datos, final String imagen) {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        final Call<String> resp = service.ActualizarImageSing(body);

        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String code = Integer.toString(response.code());

                if (response.code() != 200) {

                    if (response.code() == 400) {
                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Ocurrió un error: " + code + " ", Toast.LENGTH_LONG).show();
                        Log.e("Error", code);
                        return;
                    }

                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "¡Imagen actualizada!", Toast.LENGTH_LONG).show();
                        Log.d("JSON_IMAGEN", "La imagen se guardo correctamente");

                        sharedPreferences.removeIdImage_P_S();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }

    public void SendImagePerfil(final String datos, final String imagen) {

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.ActualizarFoto(body);

        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String code = Integer.toString(response.code());

                if (response.code() != 200) {
                    Toast.makeText(getContext(), "Ocurrió un error: " + code, Toast.LENGTH_LONG).show();
                    Log.e("Error", code);
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "¡Imagen actualizada!", Toast.LENGTH_LONG).show();
                        Log.d("JSON_IMAGEN_PERFIL", "La imagen se guardo correctamente");

                        sharedPreferences.removePerfilImage();
                        sharedPreferences.putPerfilImage(imagen);
                        Util.PERFIL_IMAGE = imagen;
                        mainActivity.actualizarPerfilImage();

                        sharedPreferences.removeIdImage_P_S();

                    }
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }

    public void cambiarDatosPersonales(String datos) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        /*imm.hideSoftInputFromWindow(nombre_nuevoPersonal.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(apellido_nevoPersonal.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(cedula_nuevoPersonal.getWindowToken(), 0);*/

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.ActualizarDatosPersonales(body);

        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getContext(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                       // String Nombre = nombre_nuevoPersonal.getText().toString() + " " + " " + apellido_nevoPersonal.getText().toString();

                        sharedPreferences.removeName();
                        /*sharedPreferences.putName(Nombre);
                        Util.NOMBRE = Nombre;*/
                        mainActivity.actualizarNombre();

                        //LimpiarCamposInfoPersonal();
                        Toast.makeText(getContext(), "¡Información personal actualizada!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }

    public void LimpiarCamposContraseña() {
        contraseña_nueva.setText("");
        contraseña_actual.setText("");
        contraseña_nueva_aye.setErrorEnabled(false);
        contraseña_actual_aye.setErrorEnabled(false);
    }

    /*public void LimpiarCamposInfoPersonal() {
        nombre_nuevoPersonal.setText("");
        apellido_nevoPersonal.setText("");
        especialidad_nuevoPersonal.setText("");
        cedula_nuevoPersonal.setText("");
        nombre_nuevoPersonal_aye.setErrorEnabled(false);
        apellido_nuevoPersonal_aye.setErrorEnabled(false);
        especialidad_nuevoPersonal_aye.setErrorEnabled(false);
        cedula_nuevoPersonal_aye.setErrorEnabled(false);
    }*/

    private void TextChangetListener() {

        contraseña_actual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contraseña_actual.setError(null);
                contraseña_actual_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contraseña_nueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contraseña_nueva.setError(null);
                contraseña_nueva_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*nombre_nuevoPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nombre_nuevoPersonal.setError(null);
                nombre_nuevoPersonal_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

       /* apellido_nevoPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                apellido_nevoPersonal.setError(null);
                apellido_nuevoPersonal_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        /*especialidad_nuevoPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                especialidad_nuevoPersonal.setError(null);
                especialidad_nuevoPersonal_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        /*cedula_nuevoPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cedula_nuevoPersonal.setError(null);
                cedula_nuevoPersonal_aye.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }
}
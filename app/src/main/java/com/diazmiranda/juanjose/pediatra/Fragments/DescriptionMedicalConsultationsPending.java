package com.diazmiranda.juanjose.pediatra.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.diazmiranda.juanjose.pediatra.Activities.ShowUniqueImage;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.GridViewAdapterImages;
import com.diazmiranda.juanjose.pediatra.Requests.Model.ImageName;
import com.diazmiranda.juanjose.pediatra.Requests.Model.PrescriptionsSpinner;
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.Util;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DescriptionMedicalConsultationsPending.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DescriptionMedicalConsultationsPending#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DescriptionMedicalConsultationsPending extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //TODO: Elements of view
    private RequestQueue queue;

    private TextView NameUser;
    private TextView NameDependienteParentezco;
    private TextView StatusMoney;
    private TextView FechaDeConsulta;
    private TextView PesoTemperatura;
    private TextView Sintomas;

    private ImageView MoneyIcon;

    private TextInputLayout nota_aye, medicamento_aye, dosis_aye, intervalo_aye, duracion_aye;
    private EditText nota, medicamento, dosis, intervalo, duracion;
    private ImageButton addMedicamento;
    private Spinner prescriptionsSpinner;
    private TextView TxvImagenAnexada;
    private GridView GridViewImages;
    private ArrayList<ImageName> list_data;
    private TableLayout tableLayoutMedicamento;

    private Button responderConsulta;

    private final static String TAG = "DescriptionMedicalConsultationsPending";
    private SharedPreferencesHelper sharedPreferences;
    private JSONArray receta = new JSONArray();
    private ArrayList<PrescriptionsSpinner> prescriptionsSpinnersArrayList;
    private ArrayList<String> nombreMedicamentoSpinner = new ArrayList<String>();


    private OnFragmentInteractionListener mListener;

    public DescriptionMedicalConsultationsPending() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescriptionMedicalConsultationsPending.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionMedicalConsultationsPending newInstance(String param1, String param2) {
        DescriptionMedicalConsultationsPending fragment = new DescriptionMedicalConsultationsPending();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = new SharedPreferencesHelper(getContext());
        View view = inflater.inflate(R.layout.f_descripcion_consulta_pendiente, container, false);
        getActivity().setTitle("Detalle de la consulta"); // Titulo del fragment
        startComponents(view); // Inicializar los componentes de la vista
        actualizarMediaToken(); // Actualizar el token que permite ver los archivos
        obtenerDetalleDeLaConsulta(); // Obtener la información de la consulta
        showPrescriptionInSpinner();
        ButtonToast(); //Acción en los botones
        TextChangetListener(); // Limpiar TextImputLayout

        return view;
    }

    private void startComponents(View view) {
        NameUser = (TextView) view.findViewById(R.id.Username);
        NameDependienteParentezco = (TextView) view.findViewById(R.id.NameDependienteParentezco);
        StatusMoney = (TextView) view.findViewById(R.id.StatusMoney);
        FechaDeConsulta = (TextView) view.findViewById(R.id.FechaDeConsulta);
        PesoTemperatura = (TextView) view.findViewById(R.id.PesoTemperatura);
        Sintomas = (TextView) view.findViewById(R.id.Sintomas);
        TxvImagenAnexada = (TextView) view.findViewById(R.id.TxvImagenAnexadaPendiente);
        MoneyIcon = (ImageView) view.findViewById(R.id.MoneyIcon);
        GridViewImages = (GridView) view.findViewById(R.id.GridViewImagesPendientes);

        nota = (EditText) view.findViewById(R.id.notaOfPediatra);
        addMedicamento = (ImageButton) view.findViewById(R.id.add_medicamento);

        prescriptionsSpinner = (Spinner) view.findViewById(R.id.prescripionSpinner);

        medicamento = (EditText) view.findViewById(R.id.medicamentoOfPediatra);
        dosis = (EditText) view.findViewById(R.id.dosisOfPediatra);
        intervalo = (EditText) view.findViewById(R.id.intervaloOfPediatra);
        duracion = (EditText) view.findViewById(R.id.duraciónOfPediatra);

        nota_aye = view.findViewById(R.id.nota_aye);
        medicamento_aye = view.findViewById(R.id.medicamento_aye);
        dosis_aye = view.findViewById(R.id.dosis_aye);
        intervalo_aye = view.findViewById(R.id.intervalo_aye);
        duracion_aye = view.findViewById(R.id.duracion_aye);

        tableLayoutMedicamento = (TableLayout) view.findViewById(R.id.tableMedicamentos);
        responderConsulta = (Button) view.findViewById(R.id.enviarRespuesta);

        queue = Volley.newRequestQueue(getActivity());
    }

    private void actualizarMediaToken() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.ObtenerTokenParaVerArchivos();

        resp.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        sharedPreferences.removeMediaToken();
                        //Guardar MEDIA_TOKEN
                        sharedPreferences.putMediaToken(jresponse.getJSONObject("data").getString("token").replaceAll("\\p{P}", ""));
                        Util.MEDIA_TOKEN = jresponse.getJSONObject("data").getString("token").replaceAll("\\p{P}", "");

                        sharedPreferences.removeMediaTokenExp();
                        sharedPreferences.putMediaTokenExp(jresponse.getJSONObject("data").getString("exp"));
                        Util.MEDIA_TOKEN_EXP = jresponse.getJSONObject("data").getString("exp");
                    }

                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void obtenerDetalleDeLaConsulta() {

        String idConsulta = Integer.toString(sharedPreferences.getidConsulta());

        String url = "http://23.82.16.144/HDK/api/consulta/" + idConsulta;
        Log.d("Detalle_De_La_Consulta", "Url lista!" + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jresponse = response;

                        try {
                            if (!jresponse.getBoolean("OK")) {
                                Log.e("Your Array Response", "Data Null");
                                Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("Your Array Response", "Correcto: " + jresponse.toString());

                                //Recuperar Id del dependiente
                                sharedPreferences.putIdDependiente(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("dependiente").getInt("id"));
                                Util.ID_DEPENDIENTE = jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("dependiente").getInt("id");

                                //Recuperar Id del usuario
                                sharedPreferences.putIdUsuario(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("usuario").getInt("id"));
                                Util.ID_USUARIO = jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("usuario").getInt("id");

                                //Recuperar imagenes ---------------------------------------------------------------------------------
                                JSONArray archivos = jresponse.getJSONObject("data").getJSONObject("consulta").getJSONArray("archivos");
                                Log.e("Your Array Response", "Correcto: " + archivos.toString());

                                if (archivos.length() == 0) {
                                    TxvImagenAnexada.setText("Sin imagenes anexadas");
                                } else {

                                    list_data = new ArrayList<>();

                                    try {

                                        for (int i = 0; i < archivos.length(); i++) {

                                            if (archivos.length() > 1) {
                                                TxvImagenAnexada.setText("Imagenes anexadas");
                                            }
                                            String Name = archivos.getString(i);
                                            ImageName imageName = new ImageName(Name);
                                            list_data.add(imageName);
                                            Log.e("Image_Name", Name);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    GridViewAdapterImages gridViewAdapterImages = new GridViewAdapterImages(getActivity(), R.layout.grid_adapter, list_data);
                                    GridViewImages.setAdapter(gridViewAdapterImages);

                                    GridViewImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            String ImageName = list_data.get(position).getImageName();
                                            sharedPreferences.removeNameImage();
                                            sharedPreferences.putNameImage(ImageName);

                                            Intent ShowUniqueImage = new Intent(getActivity(), ShowUniqueImage.class);
                                            ShowUniqueImage.putExtra("ConfirmationVisibility", false);
                                            startActivity(ShowUniqueImage);

                                        }
                                    });
                                }

                                // ------------------------------------------------------------------------------------------------

                                NameDependienteParentezco.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("usuario").getString("nombre") + " - " + jresponse.getJSONObject("data").getJSONObject("consulta").getString("parentesco"));
                                NameUser.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("dependiente").getString("nombreCompleto"));
                                if (!jresponse.getJSONObject("data").getJSONObject("consulta").getBoolean("pagado")) {
                                    StatusMoney.setText("- Sin pagar");
                                    MoneyIcon.setImageResource(R.drawable.dinerooff);
                                } else {
                                    StatusMoney.setText("- Pagado");
                                    MoneyIcon.setImageResource(R.drawable.dineroon);
                                }
                                Sintomas.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("sintomas"));
                                FechaDeConsulta.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("fecRegistro"));
                                PesoTemperatura.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("peso") + "Kg.  -  " + jresponse.getJSONObject("data").getJSONObject("consulta").getString("temperatura"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.d("Response", String.valueOf(error));
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-type", "application/json; charset=utf-8");
                params.put("Authorization", "bearer " + sharedPreferences.getToken());
                return params;
            }
        };
        queue.add(request);
        sharedPreferences.removeIdDependiente();
        sharedPreferences.removeNameImage();
    }

    private void ButtonToast() {
        addMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(medicamento.getText()) ||
                        TextUtils.isEmpty(dosis.getText()) || TextUtils.isEmpty(intervalo.getText()) ||
                        TextUtils.isEmpty(duracion.getText())) {
                    if (TextUtils.isEmpty(medicamento.getText())) {
                        medicamento_aye.setErrorEnabled(true);
                        medicamento_aye.setError("Por favor, ingrese el nombre de un medicamento");
                    } else if (TextUtils.isEmpty(dosis.getText())) {
                        dosis_aye.setErrorEnabled(true);
                        dosis_aye.setError("Por favor, ingrese la dosis de: " + medicamento.getText().toString());
                    } else if (TextUtils.isEmpty(intervalo.getText())) {
                        intervalo_aye.setErrorEnabled(true);
                        intervalo_aye.setError("Por favor, ingrese el intervalo de tiempo de cada dosis");
                    } else if (TextUtils.isEmpty(duracion.getText())) {
                        duracion_aye.setErrorEnabled(true);
                        duracion_aye.setError("Por favor, indique el periodo de tiempo en que el paciente debe consumir el medicamento");
                    }
                } else {
                    try {
                        crearReceta(
                                medicamento.getText().toString(),
                                dosis.getText().toString(),
                                intervalo.getText().toString(),
                                duracion.getText().toString());

                        cleanEditText();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        responderConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receta.length() == 0 || TextUtils.isEmpty(nota.getText())) {
                    if (receta.length() == 0) {
                        Toast.makeText(getActivity(), "¡Ups! No puede responder a la consulta sin ningún medicamento en la receta", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(nota.getText())) {
                        nota_aye.setErrorEnabled(true);
                        nota_aye.setError("Por favor, ingrese una nota médica");
                    }
                } else {
                    enviarRecetaVolley();
                }
            }
        });
    }

    private void crearReceta(String medicamento, String dosis, String intervalo, String duracion) throws JSONException {
        final JSONObject medicamentos = new JSONObject();

        medicamentos.put("medicamento", medicamento);
        medicamentos.put("dosis", dosis);
        medicamentos.put("intervalo", intervalo);
        medicamentos.put("duracion", duracion);

        receta.put(receta.length(), medicamentos);

        TableRow fila = new TableRow(getActivity());

        TextView txv_medicamento = new TextView(getActivity());
        txv_medicamento.setGravity(Gravity.CENTER);
        txv_medicamento.setText(medicamento + "   ");

        TextView txv_dosis = new TextView(getActivity());
        txv_dosis.setGravity(Gravity.CENTER);
        txv_dosis.setText(dosis + "   ");

        TextView txv_intervalo = new TextView(getActivity());
        txv_intervalo.setGravity(Gravity.CENTER);
        txv_intervalo.setText(intervalo + "   ");

        TextView txv_duracion = new TextView(getActivity());
        txv_duracion.setGravity(Gravity.CENTER);
        txv_duracion.setText(duracion + "   ");


        fila.addView(txv_medicamento);
        fila.addView(txv_dosis);
        fila.addView(txv_intervalo);
        fila.addView(txv_duracion);
        tableLayoutMedicamento.addView(fila);

        Log.e("Recuperar_Datos_Receta", receta.toString());
    }

    private void showPrescriptionInSpinner() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.ObtenerRecetas();

        resp.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray prescriptionArray = jresponse.getJSONObject("data").getJSONArray("recetas");
                        prescriptionsSpinnersArrayList = new ArrayList<>();

                        PrescriptionsSpinner prescriptionsSpinnerIntro = new PrescriptionsSpinner();
                        prescriptionsSpinnerIntro.setId(0);
                        prescriptionsSpinnerIntro.setNombre("Eliga una");
                        prescriptionsSpinnersArrayList.add(prescriptionsSpinnerIntro);
                        Log.e("prescriptionsSpinnersArrayList", prescriptionArray.toString());
                        Log.e("PRIMER_ITEM_AGREGADO", "Listo" + prescriptionArray.length());

                        for (int i = 0; i < prescriptionArray.length(); i++) {
                            JSONObject dataObj = prescriptionArray.getJSONObject(i);
                            PrescriptionsSpinner prescriptionsSpinner = new PrescriptionsSpinner();

                            prescriptionsSpinner.setId(dataObj.getInt("id"));
                            prescriptionsSpinner.setNombre(dataObj.getString("nombre"));

                            prescriptionsSpinnersArrayList.add(prescriptionsSpinner);
                        }

                        Log.e("prescriptionsSpinnersArrayList", prescriptionsSpinnersArrayList.toString());

                        for (int i = 0; i < prescriptionsSpinnersArrayList.size(); i++) {
                            nombreMedicamentoSpinner.add(prescriptionsSpinnersArrayList.get(i).getNombre());
                        }

                        ArrayAdapter<String> arrayAdapterSpinner = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                nombreMedicamentoSpinner);
                        arrayAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        prescriptionsSpinner.setAdapter(arrayAdapterSpinner);

                        prescriptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                int controller = 1;
                                while (adapterView.getItemAtPosition(controller).toString() != prescriptionsSpinnersArrayList.get(controller).getNombre()) {
                                    controller++;
                                }
                                String prescriptionCorrecto = Integer.toString(prescriptionsSpinnersArrayList.get(i).getId());
                                Log.e("PRESCRIPTION_CORRECT", prescriptionCorrecto);
                                if (prescriptionsSpinnersArrayList.get(i).getId() != 0) {
                                    obtenerDatosDeRecetaPredeterminada(prescriptionsSpinnersArrayList.get(i).getId());
                                } else {
                                    medicamento.setText(null);
                                    dosis.setText(null);
                                    duracion.setText(null);
                                    intervalo.setText(null);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG);
            }
        });
    }

    private void obtenerDatosDeRecetaPredeterminada(int id) {

        String url = "http://23.82.16.144/HDK/api/receta/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("DATOS_OBTENIDOS", response.toString());

                        String medicamento, dosis, duracion, intervalo;

                        try {
                            JSONArray answer = response.getJSONObject("data").getJSONArray("medicamentos");
                            CleanReceta();

                            for (int i = 0; i < answer.length(); i++) {
                                medicamento = answer.getJSONObject(i).getString("medicamento");
                                dosis = answer.getJSONObject(i).getString("dosis");
                                duracion = answer.getJSONObject(i).getString("duracion");
                                intervalo = answer.getJSONObject(i).getString("intervalo");

                                receta = new JSONArray();
                                crearReceta(medicamento, dosis, intervalo, duracion);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("Content-type", "application/json");
                params.put("Authorization", "bearer " + sharedPreferences.getToken());
                return params;
            }
        };
        queue.add(request);
    }

    private void CleanReceta() {
        int count = tableLayoutMedicamento.getChildCount();
        for (int i = 1; i < count; i++) {
            View child = tableLayoutMedicamento.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }

    public void enviarRecetaVolley(){
        JSONObject consulta = new JSONObject();
        try {
            consulta.put("id", sharedPreferences.getidConsulta());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject usuario = new JSONObject();
        try {
            usuario.put("id", sharedPreferences.getIdUsuario());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Llenar JSONObject de respuesta
        final JSONObject respuestaDeConsulta = new JSONObject();
        try {
            respuestaDeConsulta.put("consulta", consulta);
            respuestaDeConsulta.put("usuario", usuario);
            respuestaDeConsulta.put("detalles", receta);
            respuestaDeConsulta.put("notas", nota.getText().toString());
            Log.e("JSON_DE_RESPUESTA", respuestaDeConsulta.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://23.82.16.144/HDK/api/consulta/atender";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, respuestaDeConsulta,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (!response.getBoolean("OK")) {
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("CONSULTA_ATENDIDA", response.toString());
                                Toast.makeText(getContext(), "¡Consulta finalizada!", Toast.LENGTH_LONG).show();

                                MedicalConsultationsPending fragment = new MedicalConsultationsPending();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.Vista, fragment, "Consultas Pendientes");
                                fragmentTransaction.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                params.put("Authorization", "bearer " + sharedPreferences.getToken());
                return params;
            }
        };
        queue.add(request);
    }

    public void TextChangetListener() {
        nota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nota.setError(null);
                nota_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        medicamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                medicamento.setError(null);
                medicamento_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dosis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dosis.setError(null);
                dosis_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        intervalo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                intervalo.setError(null);
                intervalo_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        duracion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                duracion.setError(null);
                duracion_aye.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void cleanEditText() {
        nota.setText(null);
        medicamento.setText(null);
        dosis.setText(null);
        intervalo.setText(null);
        duracion.setText(null);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
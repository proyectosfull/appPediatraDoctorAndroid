package com.diazmiranda.juanjose.pediatra.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DescriptionMedicalConsultationsMade.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DescriptionMedicalConsultationsMade#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionMedicalConsultationsMade extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferencesHelper sharedPreferences;
    private RequestQueue queue;


    private TextView userName, nameDepentienteParentezco, statusMoney, fechaConsulta, fechaConsultaRealizada, pesoTemperatura, descripcion;
    private ImageView MoneyIcon;
    private TextView TxvImagenAnexada;
    private GridView GridViewImages;
    private ArrayList<ImageName> list_data;
    private TableLayout resultReceta;

    private OnFragmentInteractionListener mListener;

    public DescriptionMedicalConsultationsMade() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescriptionMedicalConsultationsMade.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionMedicalConsultationsMade newInstance(String param1, String param2) {
        DescriptionMedicalConsultationsMade fragment = new DescriptionMedicalConsultationsMade();
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
        View view = inflater.inflate(R.layout.f_descripcion_consulta_realizada, container, false);
        startComponents(view);
        actualizarMediaToken();
        obtenerDetalleDeLaConsulta();
        return view;
    }

    private void startComponents(View view) {
        userName = (TextView) view.findViewById(R.id.DCR_Username);
        nameDepentienteParentezco = (TextView) view.findViewById(R.id.DCR_NameDependienteParentezco);
        statusMoney = (TextView) view.findViewById(R.id.DCR_StatusMoney);
        fechaConsulta = (TextView) view.findViewById(R.id.DCR_FechaDeConsulta);
        fechaConsultaRealizada = (TextView) view.findViewById(R.id.DCR_FechaDeConsultaRealizada);
        pesoTemperatura = (TextView) view.findViewById(R.id.DCR_PesoTemperatura);
        descripcion = (TextView) view.findViewById(R.id.DCR_Sintomas);
        TxvImagenAnexada = (TextView) view.findViewById(R.id.TxvImagenAnexada);
        MoneyIcon = (ImageView) view.findViewById(R.id.DCR_MoneyIcon);

        GridViewImages = (GridView) view.findViewById(R.id.GridViewImages);
        resultReceta = (TableLayout) view.findViewById(R.id.resultOfReceta);

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
                                Log.e("Your Array Response", "DATA: " + jresponse.toString());

                                sharedPreferences.putIdDependiente(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("dependiente").getInt("id"));
                                Util.ID_DEPENDIENTE = jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("dependiente").getInt("id");

                                //Recuperar imagenes ---------------------------------------------------------------------------------
                                JSONArray archivos = jresponse.getJSONObject("data").getJSONObject("consulta").getJSONArray("archivos");
                                Log.e("Your Array Response", "ARCHIVOS: " + archivos.toString());

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

                                // RECUPERAR RECETA (MEDICAMENTOS)
                                JSONArray jsonReceta = jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("receta").getJSONArray("detalles");
                                Log.e("RECETA_MEDICAMENTOS", jsonReceta.toString());

                                for (int i = 0; i < jsonReceta.length(); i++) {


                                    TableRow fila = new TableRow(getActivity());

                                    TextView txv_medicamento = new TextView(getActivity());
                                    txv_medicamento.setGravity(Gravity.CENTER);
                                    txv_medicamento.setText(jsonReceta.getJSONObject(i).getString("medicamento") + "  ");

                                    TextView txv_dosis = new TextView(getActivity());
                                    txv_dosis.setGravity(Gravity.CENTER);
                                    txv_dosis.setText(jsonReceta.getJSONObject(i).getString("dosis") + "  ");

                                    TextView txv_intervalo = new TextView(getActivity());
                                    txv_intervalo.setGravity(Gravity.CENTER);
                                    txv_intervalo.setText(jsonReceta.getJSONObject(i).getString("intervalo") + "  ");

                                    TextView txv_duracion = new TextView(getActivity());
                                    txv_duracion.setGravity(Gravity.CENTER);
                                    txv_duracion.setText(jsonReceta.getJSONObject(i).getString("duracion") + "  ");


                                    fila.addView(txv_medicamento);
                                    fila.addView(txv_dosis);
                                    fila.addView(txv_intervalo);
                                    fila.addView(txv_duracion);
                                    resultReceta.addView(fila);

                                }

                                // -----------------------------------------------------------------
                                nameDepentienteParentezco.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("usuario").getString("nombre") + " - " + jresponse.getJSONObject("data").getJSONObject("consulta").getString("parentesco"));
                                userName.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getJSONObject("dependiente").getString("nombreCompleto"));
                                if (!jresponse.getJSONObject("data").getJSONObject("consulta").getBoolean("pagado")) {
                                    statusMoney.setText("- Sin pagar");
                                    MoneyIcon.setImageResource(R.drawable.dinerooff);
                                } else {
                                    statusMoney.setText("- Pagado");
                                    MoneyIcon.setImageResource(R.drawable.dineroon);
                                }
                                descripcion.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("sintomas"));
                                fechaConsulta.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("fecRegistro"));
                                fechaConsultaRealizada.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("fecAtendido"));
                                pesoTemperatura.setText(jresponse.getJSONObject("data").getJSONObject("consulta").getString("peso") + "Kg.  -  " + jresponse.getJSONObject("data").getJSONObject("consulta").getString("temperatura"));
                            }
                        } catch (
                                JSONException e) {
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
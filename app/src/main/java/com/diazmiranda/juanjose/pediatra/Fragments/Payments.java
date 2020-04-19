package com.diazmiranda.juanjose.pediatra.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diazmiranda.juanjose.pediatra.Activities.ShowUniqueImage;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.MedicalConsultationPayment;
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RecyclerViewAdapterMCP;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.UI;
import com.diazmiranda.juanjose.pediatra.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Payments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Payments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Payments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferencesHelper sharedPreferences;
    private RecyclerView recyclerViewPayment;
    private RecyclerViewAdapterMCP adapterMCP;
    private List<MedicalConsultationPayment> data_list;
    private ProgressDialog progressDialog;

    public Payments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Payments.
     */
    // TODO: Rename and change types and number of parameters
    public static Payments newInstance(String param1, String param2) {
        Payments fragment = new Payments();
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
        sharedPreferences = new SharedPreferencesHelper(getActivity());
        View view = inflater.inflate(R.layout.f_payments, container, false);
        recyclerViewPayment = view.findViewById(R.id.recyclerViewPayment);
        recyclerViewPayment.setHasFixedSize(true);

        actualizarMediaToken();
        ObtainPaymentData();

        return view;
    }

    public void ObtainPaymentData() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.ObtainPaymentData();
        progressDialog = UI.showWaitDialog(getActivity());
        resp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getContext(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    JSONArray payments = jresponse.getJSONObject("data").getJSONArray("pagos");

                    data_list = new ArrayList<>();
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else if (payments.length() == 0) {
                        progressDialog.dismiss();
                        ResponseNull fragment = new ResponseNull();

                        Bundle bundle = new Bundle();
                        bundle.putString("Message", "Lamentablemente aún no cuenta con ningún pago.");
                        fragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.Vista, fragment, "MedicalConsultation Pendientes");
                        fragmentTransaction.commit();
                    } else {
                        for (int i = 0; i < payments.length(); i++) {
                            JSONObject data = payments.getJSONObject(i);
                            MedicalConsultationPayment medicalConsultationPayment = new MedicalConsultationPayment(
                                    data.getString("nombre"),
                                    data.getJSONObject("consulta").getString("fecRegistro"),
                                    data.getJSONObject("consulta").getJSONObject("usuario").getString("nombreCompleto"),
                                    data.getJSONObject("consulta").getJSONObject("dependiente").getString("nombreCompleto"),
                                    data.getJSONObject("consulta").getJSONObject("dependiente").getInt("id"),
                                    data.getJSONObject("consulta").getInt("id")
                            );
                            data_list.add(medicalConsultationPayment);
                        }
                        progressDialog.dismiss();
                        adapterMCP = new RecyclerViewAdapterMCP(getActivity(), data_list);

                        adapterMCP.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view) {
                                int id_consulta = data_list.get(
                                        recyclerViewPayment.getChildAdapterPosition(view)).getIdPayment();

                                int IdDependiente = data_list.get(
                                        recyclerViewPayment.getChildAdapterPosition(view)).getIdDependiente();

                                sharedPreferences.putIdItem(recyclerViewPayment.getChildAdapterPosition(view));
                                Util.ITEM = recyclerViewPayment.getChildAdapterPosition(view);
                                Log.e("POSICIÓN", Integer.toString(sharedPreferences.getidImtem()));

                                String ImageName = data_list.get(recyclerViewPayment.getChildAdapterPosition(view)).getNombre();

                                //ID Consulta
                                String idConsultaString = Integer.toString(sharedPreferences.getidConsulta());
                                sharedPreferences.putIdConsulta(id_consulta);

                                //Id Dependiente
                                sharedPreferences.putIdDependiente(IdDependiente);

                                // Image Name
                                sharedPreferences.putNameImage(ImageName);

                                Log.e("IdConsulta / IdDependiente", idConsultaString + " " + IdDependiente);

                                GoShowUniqueImage(data_list);

                            }
                        });

                        recyclerViewPayment.setAdapter(adapterMCP);
                        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                progressDialog.dismiss();
            }
        });
    }

    public void UpdateRecyclerView() {

        sharedPreferences.getidImtem();
        recyclerViewPayment.removeViewAt(sharedPreferences.getidImtem());
        adapterMCP.notifyItemRemoved(sharedPreferences.getidImtem());
        adapterMCP.notifyItemRangeChanged(sharedPreferences.getidImtem(), data_list.size());
        adapterMCP.notifyDataSetChanged();

        sharedPreferences.removeUpdatePayment();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

                        //Guardar MEDIA_TOKE_EXP
                        sharedPreferences.removeMediaTokenExp();
                        sharedPreferences.putMediaTokenExp(jresponse.getJSONObject("data").getString("exp"));
                        Util.MEDIA_TOKEN_EXP = jresponse.getJSONObject("data").getString("exp");

                        Log.e("MEDIA_TOKEN", sharedPreferences.getMediaToken());
                        Log.e("MEDIA_TOKEN_EXP", sharedPreferences.getMediaTokenExp());
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

    private void GoShowUniqueImage(List<MedicalConsultationPayment> data_list) {
        Intent ShowUniqueImage = new Intent(getActivity(), ShowUniqueImage.class);
        ShowUniqueImage.putExtra("ConfirmationVisibility", true);
        ShowUniqueImage.putExtra("data", data_list.toString());
        startActivity(ShowUniqueImage);
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
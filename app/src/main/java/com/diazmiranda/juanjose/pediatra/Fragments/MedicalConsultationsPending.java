package com.diazmiranda.juanjose.pediatra.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.MedicalConsultation;
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RecyclerViewAdapterMC;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.pediatra.Util.UI;

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
 * {@link MedicalConsultationsPending.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicalConsultationsPending#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalConsultationsPending extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferencesHelper sharedPreferences;
    private RecyclerView recyclerViewCitas;
    private RecyclerViewAdapterMC adaptadorCitas;
    private List<MedicalConsultation> data_list;
    private ProgressDialog progressDialog;

    public MedicalConsultationsPending() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicalConsultationsPending.
     */

    public static MedicalConsultationsPending newInstance(String param1, String param2) {
        MedicalConsultationsPending fragment = new MedicalConsultationsPending();
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
        View view = inflater.inflate(R.layout.f_medical_consultations, container, false);
        sharedPreferences = new SharedPreferencesHelper(getActivity());
        getActivity().setTitle("Consultas pendientes");
        recyclerViewCitas = view.findViewById(R.id.recyclerViewCitas);
        recyclerViewCitas.setHasFixedSize(true);
        ConsultarCitasPendientes();
        return view;
    }

    private void ConsultarCitasPendientes() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.ObtenerConsultasPendientes();
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
                    JSONArray citas = jresponse.getJSONObject("data").getJSONArray("consultas");
                    //sharedPreferences.putIdConsulta(citas.);
                    data_list = new ArrayList<>();

                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                    } else if (citas.length() == 0) {
                        progressDialog.dismiss();
                        ResponseNull fragment = new ResponseNull();

                        Bundle bundle = new Bundle();
                        bundle.putString("Message", "¡Ups! Al parecer no tiene ninguna consulta pendiente.");
                        fragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.Vista, fragment, "MedicalConsultation Pendientes");
                        fragmentTransaction.commit();
                    } else {
                        for (int i = 0; i < citas.length(); i++) {
                            JSONObject data = citas.getJSONObject(i);
                            MedicalConsultation cita = new MedicalConsultation(
                                    data.getString("parentesco"),
                                    data.getJSONObject("dependiente").getString("nombreCompleto"),
                                    data.getJSONObject("usuario").getString("nombre"),
                                    data.getInt("id"));
                            data_list.add(cita);
                        }
                        progressDialog.dismiss();
                        adaptadorCitas = new RecyclerViewAdapterMC(data_list, getActivity());

                        adaptadorCitas.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int id_consulta = data_list.get(
                                        recyclerViewCitas.getChildAdapterPosition(view)).getId();

                                String idConsultaString = Integer.toString(sharedPreferences.getidConsulta());
                                sharedPreferences.putIdConsulta(id_consulta);
                                Log.d("ID_CONSULTA", idConsultaString);

                                DescriptionMedicalConsultationsPending fragment = new DescriptionMedicalConsultationsPending();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.Vista, fragment, "Descripcion Consulta Pendiente");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });

                        recyclerViewCitas.setAdapter(adaptadorCitas);
                        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(getActivity()));
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
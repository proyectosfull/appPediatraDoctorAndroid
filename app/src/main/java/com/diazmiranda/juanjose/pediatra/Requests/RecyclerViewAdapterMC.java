package com.diazmiranda.juanjose.pediatra.Requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.MedicalConsultation;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;

import java.util.List;

public class RecyclerViewAdapterMC extends RecyclerView.Adapter<RecyclerViewAdapterMC.ViewHolder> implements View.OnClickListener {

    private SharedPreferencesHelper sharedPreferences;
    private Context context;
    private List<MedicalConsultation> my_data;
    private View.OnClickListener listener;
    private int idConsulta;
    private String idConsultaString;

    public RecyclerViewAdapterMC(List<MedicalConsultation> my_data, Context context) {
        this.my_data = my_data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_medical_consultations, viewGroup, false);
        sharedPreferences = new SharedPreferencesHelper(context);
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.dependiente.setText(my_data.get(i).getNombreCompleto()  + " - " + my_data.get(i).getParentesco());
        viewHolder.usuario.setText(my_data.get(i).getNombre());

        idConsulta = my_data.get(i).getId();
        idConsultaString = Integer.toString(idConsulta);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView usuario, dependiente;
        public ImageView photo, money;
        public CardView cardViewCita;

        public ViewHolder(View view) {
            super(view);
            cardViewCita = (CardView) view.findViewById(R.id.card_citap);
            usuario = (TextView) view.findViewById(R.id.txv_nombreUsuario);
            dependiente = (TextView) view.findViewById(R.id.txv_dependiente);
            photo = (ImageView) view.findViewById(R.id.img_pasciente);
        }
    }
}
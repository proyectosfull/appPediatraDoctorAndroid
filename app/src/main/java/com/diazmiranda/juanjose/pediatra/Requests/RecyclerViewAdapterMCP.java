package com.diazmiranda.juanjose.pediatra.Requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.MedicalConsultationPayment;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;

import java.util.List;

public class RecyclerViewAdapterMCP extends RecyclerView.Adapter<RecyclerViewAdapterMCP.ViewHolder> implements View.OnClickListener{

    private SharedPreferencesHelper sharedPreferences;
    private Context context;
    private List<MedicalConsultationPayment> myData;
    private View.OnClickListener listener;

    public RecyclerViewAdapterMCP(Context context, List<MedicalConsultationPayment> myData) {
        this.context = context;
        this.myData = myData;
    }

    public RecyclerViewAdapterMCP(){

    }

    @NonNull
    @Override
    public RecyclerViewAdapterMCP.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_payments, viewGroup, false);
        sharedPreferences = new SharedPreferencesHelper(context);
        view.setOnClickListener(this);

        return new RecyclerViewAdapterMCP.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMCP.ViewHolder viewHolder, int i) {
        viewHolder.nombre.setText(myData.get(i).getNombrePaciente());
        viewHolder.fechaDePago.setText(myData.get(i).getFecRegistro());
        viewHolder.nombreUser.setText(myData.get(i).getNombreCompleto());
    }

    @Override
    public int getItemCount() {
        return myData.size();
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

        public TextView nombre, fechaDePago, nombreUser;
        public CardView cardViewPaymaent;

        public ViewHolder(View view) {
            super(view);
            cardViewPaymaent = (CardView) view.findViewById(R.id.card_pagosDeConsulta);
            nombre = (TextView) view.findViewById(R.id.txv_nombreDelPago);
            fechaDePago = (TextView) view.findViewById(R.id.txv_fechaDePago);
            nombreUser = (TextView) view.findViewById(R.id.txv_nombreUserPago);
        }
    }
}
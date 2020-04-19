package com.diazmiranda.juanjose.pediatra.Requests;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.ImageName;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapterImages extends ArrayAdapter<ImageName> {
    private SharedPreferencesHelper sharedPreferences;
    ArrayList<ImageName> listdata;
    Context context;
    int resource;

    public GridViewAdapterImages(@NonNull Context context, int resource, @NonNull ArrayList<ImageName> listdata) {
        super(context, resource, listdata);
        this.listdata=listdata;
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_adapter,null,true);

            sharedPreferences = new SharedPreferencesHelper(getContext());
            ImageName listdata = getItem(position);
            ImageView img = (ImageView)convertView.findViewById(R.id.DetalleImage);
            final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progress);

            final String url = "http://23.82.16.144/HDK/api/multimedia/consulta/" +
                    sharedPreferences.getIdDependiente() + "/" +
                    sharedPreferences.getMediaTokenExp() + "/" +
                    sharedPreferences.getMediaToken() + "/" +
                    listdata.getImageName();
            Log.d("URL", url);
            Picasso.with(context).load(url).resize(300,0).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        return convertView;
    }
}
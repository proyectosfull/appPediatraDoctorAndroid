package com.diazmiranda.juanjose.pediatra.Util;

import android.app.ProgressDialog;
import android.content.Context;

public class UI {

    public static ProgressDialog showWaitDialog(Context ctx) {
        return showWaitDialog(ctx, "Espere un momento por favor...");
    }

    public static ProgressDialog showWaitDialog(Context ctx, String mensaje) {

        ProgressDialog pDialog = new ProgressDialog(ctx);
        try {
            pDialog.setMessage(mensaje);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pDialog;
    }
}
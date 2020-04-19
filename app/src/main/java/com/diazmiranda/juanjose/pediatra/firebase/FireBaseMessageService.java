package com.diazmiranda.juanjose.pediatra.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.diazmiranda.juanjose.pediatra.Activities.LoginActivity;
import com.diazmiranda.juanjose.pediatra.R;
import com.diazmiranda.juanjose.pediatra.Requests.Model.UsuarioService;
import com.diazmiranda.juanjose.pediatra.Requests.RetrofitRequest;
import com.diazmiranda.juanjose.pediatra.Util.SharedPreferencesHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class FireBaseMessageService extends FirebaseMessagingService {
    private static final String channelId = "PediatraMessageNotification";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(getApplicationContext());
        sharedPreferences.putIsTokenUpdated(false);
        sharedPreferences.putFCMToken(token);
        updateFCMToken(sharedPreferences, token);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> item = remoteMessage.getData();

        switch (item.get("opt")) {
            case "nuevaConsulta":
                sendNotification(item.get("title"), item.get("body"));
                Log.d("onMessageReceived", "MESSAGE RECEIVEDX2!!!");
                break;
        }
    }

    private void sendNotification(String title, String body) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_name);
            String description = getString(R.string.notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Random random = new Random();
        MessageNotification.title = title;
        MessageNotification.text = body;
        MessageNotification.icon = R.mipmap.ic_launcher;

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        MessageNotification.notify(getApplicationContext(), random.nextInt(100), intent, channelId);
    }

    private void updateFCMToken(final SharedPreferencesHelper preferencesHelper, String token) {
        if (preferencesHelper.getToken() == null)
            return;
        try {
            JSONObject datos = new JSONObject();
            datos.put("FCMToken", token);

            UsuarioService service = RetrofitRequest.create(UsuarioService.class);
            RequestBody body = RetrofitRequest.createBody(datos.toString());
            Call<String> resp = service.login(body);

            Response<String> response = resp.execute();
            if (response.code() != 200) {
                return;
            }
            JSONObject jres = new JSONObject(response.body());
            boolean wasUpdated = jres.getBoolean("OK");
            preferencesHelper.putIsTokenUpdated(wasUpdated);
        } catch (IOException | JSONException e) {
        }
    }
}


/*public class FireBaseMessageService extends FirebaseMessagingService {
    private static final String channelId = "com.diazmiranda.juanjose.mibebe.consultas";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(getApplicationContext());
        sharedPreferences.putIsTokenUpdated(false);
        sharedPreferences.putFCMToken(token);
        updateFCMToken(sharedPreferences, token);
    }

    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        Map<String, String> item = remoteMessage.getData();

        //LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        //Intent intent = new Intent( MainActivity.REQUEST_ACCEPT);

        switch(item.get("opt")) {
            case "consultaRealizada": //consultaRealizada
                //String image = item.get("image");



                //AppDatabase.getInstance(getApplicationContext()).anounncement().insert(anounncement);
                sendNotification(item.get("title"), item.get("body"));

                //intent.putExtra(MainActivity.PARAM_TYPE, MainActivity.ANNOUNCEMENT);
                //localBroadcastManager.sendBroadcast(intent);
                break;
        }
    }

    private void sendNotification(String title, String body) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = getString( R.string.notification_name);
            String description = getString(R.string.notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        MessageNotification.title = title;
        MessageNotification.text = body;
        MessageNotification.icon = R.mipmap.ic_launcher;

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent.putExtra(AnounncementActivity.PARAM1, anounncement);
        MessageNotification.notify(getApplicationContext(), random.nextInt(100), intent, channelId);
    }

*/
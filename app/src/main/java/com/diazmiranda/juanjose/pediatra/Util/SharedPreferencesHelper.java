package com.diazmiranda.juanjose.pediatra.Util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

public class SharedPreferencesHelper {

    private static final String KEY = "_pediatra_";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public void putFCMToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken", token);
        editor.apply();
    }

    public String getFCMToken() {
        return sharedPreferences.getString("FCMToken", null);
    }

    public void removeFCMToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("FCMToken");
        editor.apply();
    }

    public void putToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token", token);
        editor.apply();
    }

    public void putIsTokenUpdated(Boolean updated) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTokenUpdated", updated);
        editor.apply();
    }

    public void removeToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Token");
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("Token", null);
    }

    public void putMediaToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MediaToken", token);
        editor.apply();
    }

    public String getMediaToken() {
        return sharedPreferences.getString("MediaToken", null);
    }

    public void removeMediaToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("MediaToken");
        editor.apply();
    }

    public void putMediaTokenExp(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MediaTokenExp", token);
        editor.apply();
    }

    public String getMediaTokenExp() {
        return sharedPreferences.getString("MediaTokenExp", null);
    }

    public void removeMediaTokenExp() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("MediaTokenExp");
        editor.apply();
    }

    public void putPersonalData(String Nombre, String Correo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Nombre", Nombre);
        editor.putString("Correo", Correo);
        editor.apply();
    }

    public void putName(String Nombre) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Nombre", Nombre);
        editor.apply();
    }

    public String getNombre() {
        return sharedPreferences.getString("Nombre", null);
    }

    public void removeName() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Nombre");
        editor.apply();
    }

    public String getCorreo() {
        return sharedPreferences.getString("Correo", null);
    }

    public void putPerfilImage(String perfilImage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("perfilImage", perfilImage);
        editor.apply();
    }

    public void removePerfilImage() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("perfilImage");
        editor.apply();
    }

    public String getPerfilImage() {
        return sharedPreferences.getString("perfilImage", null);
    }

    //KeyForConsultas
    public void putIdDependiente(int idDependiente){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idDependiente", idDependiente);
        editor.apply();
    }

    public void removeIdDependiente() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idDependiente");
        editor.apply();
    }

    public int getIdUsuario() {
        return sharedPreferences.getInt("idUsuario", 0);
    }

    public void putIdUsuario(int idUsuario){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUsuario", idUsuario);
        editor.apply();
    }

    public void removeIdUsuario() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idUsuario");
        editor.apply();
    }

    public int getIdDependiente() {
        return sharedPreferences.getInt("idDependiente", 0);
    }

    public void putNameImage(String nameImage){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nameImage", nameImage);
        editor.apply();
    }

    public void removeNameImage() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("nameImage");
        editor.apply();
    }

    public String getNameImage() {
        return sharedPreferences.getString("nameImage", null);
    }

    public void putIdConsulta(int idConsulta){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idConsulta", idConsulta);
        editor.apply();
    }

    public void removeIdConsulta() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idConsulta");
        editor.apply();
    }

    public int getidConsulta() {
        return sharedPreferences.getInt("idConsulta", 0);
    }


    public void putIdImage_P_S(int idImage){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idImage", idImage);
        editor.apply();
    }

    public void removeIdImage_P_S() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idImage");
        editor.apply();
    }

    public int getidImage_P_S() {
        return sharedPreferences.getInt("idImage", 0);
    }

    public void putIdItem(int idItem){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idItem", idItem);
        editor.apply();
    }

    public void removeItem() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idItem");
        editor.apply();
    }

    public int getidImtem() {
        return sharedPreferences.getInt("idItem", -1);
    }

    public void putUpdatePayment(Boolean UpdatePayment){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("UpdatePayment", UpdatePayment);
        editor.apply();
    }

    public void removeUpdatePayment() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("UpdatePayment");
        editor.apply();
    }

    public Boolean getUpdatePayment() {
        return sharedPreferences.getBoolean("UpdatePayment", false);
    }

    public void putNameImageSing(String nameImageSing){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nameImageSing", nameImageSing);
        editor.apply();
    }

    public void removeNameImageSing() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("nameImageSing");
        editor.apply();
    }

    public String getNameImageSing() {
        return sharedPreferences.getString("nameImageSing", null);
    }
}
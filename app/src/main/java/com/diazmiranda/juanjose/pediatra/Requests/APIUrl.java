package com.diazmiranda.juanjose.pediatra.Requests;

public abstract class APIUrl {

    //public static final String BASE = "http://209.58.143.105/HDK/api/";
    public static final String BASE = "http://23.82.16.144:8080/HDK/api/";

    public static final String LOGIN = "auth/pediatra/login";
    public static final String LOGOUT = "auth/pediatra/logout";
    public static final String REGISTRO = "pediatra/nuevo";
    public static final String CAMBIARPASSWORD = "auth/pediatra/password-restore";
    public static final String ACTUALIZARDATOSPERSONALES = "pediatra/actualizar";
    public static final String ACTUALIZARFOTO = "pediatra/actualizar-foto64";
    public static final String OBTENER_CONSULTAS_REALIZADAS = "consulta/lista?status=1";
    public static final String OBTENER_CONSULTAS_PENDIENTES = "consulta/lista?status=0";
    public static final String OBTENER_TOKEN_PARA_VER_ARCHIVOS = "auth/mediatoken";
    public static final String OBTENER_RECETAS = "receta";
    public static final String OBTENER_PAYMENT_DATA = "consulta/pagos";
    public static final String ACTUALIZAR_IMAGE_SING = "pediatra/firma";
    public static final String ACTUALIZAR_COSTO_CONSULTA = "pediatra/costoconsulta";
}
package com.diazmiranda.juanjose.pediatra.Requests.Model;

import com.diazmiranda.juanjose.pediatra.Requests.APIUrl;
import com.diazmiranda.juanjose.pediatra.Util.Constants;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UsuarioService {

    @POST(APIUrl.LOGIN)
    Call<String> login(@Body RequestBody data);

    @POST(APIUrl.REGISTRO)
    Call<String> registrar(@Body RequestBody data);

    @POST(APIUrl.LOGOUT)
    @Headers(Constants.AUTH)
    Call<String> logout();

    @POST(APIUrl.CAMBIARPASSWORD)
    @Headers(Constants.AUTH)
    Call<String> cambiarPassword(@Body RequestBody data);

    @POST(APIUrl.ACTUALIZARDATOSPERSONALES)
    @Headers(Constants.AUTH)
    Call<String> ActualizarDatosPersonales(@Body RequestBody data);

    @PUT(APIUrl.ACTUALIZARFOTO)
    @Headers(Constants.AUTH)
    Call<String> ActualizarFoto(@Body RequestBody data);

    @GET(APIUrl.OBTENER_CONSULTAS_REALIZADAS)
    @Headers(Constants.AUTH)
    Call<String> ObtenerConsultasRealizadas();

    @GET(APIUrl.OBTENER_CONSULTAS_PENDIENTES)
    @Headers(Constants.AUTH)
    Call<String> ObtenerConsultasPendientes();

    @GET(APIUrl.OBTENER_TOKEN_PARA_VER_ARCHIVOS)
    @Headers(Constants.AUTH)
    Call<String> ObtenerTokenParaVerArchivos();

    @GET(APIUrl.OBTENER_RECETAS)
    @Headers(Constants.AUTH)
    Call<String> ObtenerRecetas();

    @GET(APIUrl.OBTENER_PAYMENT_DATA)
    @Headers(Constants.AUTH)
    Call<String> ObtainPaymentData();

    @PUT(APIUrl.ACTUALIZAR_IMAGE_SING)
    @Headers(Constants.AUTH)
    Call<String> ActualizarImageSing(@Body RequestBody data);

    @POST(APIUrl.ACTUALIZAR_COSTO_CONSULTA)
    @Headers(Constants.AUTH)
    Call<String> ActualizarCostoConsulta(@Body RequestBody data);
}
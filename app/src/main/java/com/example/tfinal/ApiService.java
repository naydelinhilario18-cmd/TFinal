package com.example.tfinal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/residuos/sincronizar")
    Call<Void> enviarResiduo(@Body RegistroModelo registro);
}
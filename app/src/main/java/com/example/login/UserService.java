package com.example.login;

import android.service.autofill.AutofillService;

import org.json.JSONObject;

import java.lang.ref.Reference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
        @GET("api/has_user")
        Call<ResponseBody> authenticate(@Query("username") String user);


        @POST("api/create_account")
        Call<ResponseBody> createUser(@Body String json);

}

package com.example.login;

import org.json.JSONObject;

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
    Call<ResponseBody> authenticate(@Query("username") String user, @Query("password") String password);

    @GET("api/get_question")
    Call<ResponseBody> get_questions(@Query("id") String id);

    @POST("api/create_account")
    Call<ResponseBody> createUser(@Body String json);

    @POST("api/register_game")
    Call<ResponseBody> new_game(@Body JSONObject json);

    @POST("api/game_done")
    Call<ResponseBody> finish_game(@Body JSONObject json);

    @POST("/api/add_question")
    Call<ResponseBody> add_questionAndAnswers(@Body String qAndA);

    @GET("/api/profile")
    Call<ResponseBody> get_profile(@Query("username") String user);


}

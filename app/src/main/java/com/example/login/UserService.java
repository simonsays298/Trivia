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

    @GET("api/get_winner")
    Call<ResponseBody> get_winner(@Query("id") String id);

    @GET("api/get_rooms")
    Call<ResponseBody> get_rooms();

    @GET("api/found_opponent")
    Call<ResponseBody> found_opponent(@Query("id") String id);

    @POST("api/create_account")
    Call<ResponseBody> createUser(@Body String json);

    @POST("api/choose_room")
    Call<ResponseBody> chooseRoom(@Body JSONObject json);

    @POST("api/register_game")
    Call<ResponseBody> new_game(@Body JSONObject json);

    @POST("api/game_done")
    Call<ResponseBody> finish_game(@Body JSONObject json);

    @POST("/api/add_question")
    Call<ResponseBody> add_questionAndAnswers(@Body String qAndA);

    @GET("/api/profile")
    Call<ResponseBody> get_profile(@Query("username") String user);

    @POST("api/add_friend")
    Call<ResponseBody> add_friend(@Body String friend);

    @GET("api/get_friends")
    Call<ResponseBody> get_friends(@Query("username") String user);

    @GET("api/get_suggested_questions")
    Call<ResponseBody> get_suggested_questions();

    @POST("api/rate_question")
    Call<ResponseBody> rate_question(@Body String question);


}

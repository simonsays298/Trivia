package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    TextView userNameTextView;
    TextView numberPointsTextView;
    TextView numberFriendsTextView;

    String userName;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = getIntent().getStringExtra("USERNAME");

        //startActivity(intent);

        userNameTextView = findViewById(R.id.userNameInProfile);
        userNameTextView.setText(userName);

        numberPointsTextView = findViewById(R.id.numberPoints);
        numberFriendsTextView = findViewById(R.id.numberFriends);

        Call<ResponseBody> mService = service.get_profile(userName);

        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                String pointsAndFriends = null;
                try {
                    pointsAndFriends = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject obj = null;
                try {
                    assert pointsAndFriends != null;
                    obj = new JSONObject(pointsAndFriends);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    numberPointsTextView.setText(obj.getString("points"));
                    numberFriendsTextView.setText(obj.getString("friends"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("FAIL PROFILE ACTIVITY", t.getMessage());

            }
        });

//
//        numberPointsTextView.setText("My Awesome Text");
//        numberFriendsTextView.setText("My Awesome Text");

    }
}
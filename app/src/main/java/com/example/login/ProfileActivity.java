package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

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

    DayNightSwitch dayNightSwitch;

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

        this.setTitle(getString(R.string.view_profile));
        userName = getIntent().getStringExtra("USERNAME");

        dayNightSwitch = findViewById(R.id.day_night_switch);

        dayNightSwitch.setDuration(450);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (is_night) {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);

                }
            }
        });

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

    }

    private void toggleTheme(boolean prefTheme) {
        SharedPreferences.Editor editor = getSharedPreferences("pref", MODE_PRIVATE).edit();
        editor.putBoolean("DarkTheme", prefTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }
}
package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private TextView numberPointsTextView;
    private TextView numberFriendsTextView;

    private DayNightSwitch dayNightSwitch;

    public static final String KEY_DAY_NIGHT_SWITCH_STATE = "day_night_switch_state";

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
        checkForInvites();
        
        dayNightSwitch = findViewById(R.id.day_night_switch);
        dayNightSwitch.setDuration(450);

        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (is_night) {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);

//                    // it will set isDarkModeOn boolean to true
//                    editor.putBoolean("isDarkModeOn", true);
//                    editor.apply();

                } else {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);

//                    // it will set isDarkModeOn boolean to fals
//                    editor.putBoolean("isDarkModeOn", false);
//                    editor.apply();

                }
            }
        });

        if (savedInstanceState != null
                && savedInstanceState.containsKey(KEY_DAY_NIGHT_SWITCH_STATE))
            dayNightSwitch.setIsNight(savedInstanceState.getBoolean(KEY_DAY_NIGHT_SWITCH_STATE), true);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_DAY_NIGHT_SWITCH_STATE, dayNightSwitch.isNight());
    }

    public void checkForInvites() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firsttry-272817.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        final JSONObject[] invites = new JSONObject[1];
        final String[] res = new String[1];
        Call<ResponseBody> mService = service.get_invites(userName);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                invites[0] = null;
                res[0] = null;
                try {
                    res[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    invites[0] = new JSONObject(res[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int no_rooms = 0;
                try {
                    no_rooms = Integer.parseInt(invites[0].getString("no_rooms"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (no_rooms > 0) {

                    try {
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this)
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_info)
                                //set title
                                .setTitle("You\'ve got new invites!")
                                //set positive button
                                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what would happen when positive button is clicked
                                        Intent intent = new Intent(ProfileActivity.this, InviteRoom.class);
                                        intent.putExtra("USERNAME", userName);
                                        intent.putExtra("MULTI", "1");
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        dialogInterface.dismiss();

                                    }
                                })
                                //set negative button
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .show();
                    } catch (WindowManager.BadTokenException ex) {
                        ex.printStackTrace();
                    }



//
                } else {


                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            checkForInvites();
                        }
                    }, 3000);


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });


    }


}
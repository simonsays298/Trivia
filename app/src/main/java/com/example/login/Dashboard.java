package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Dashboard extends AppCompatActivity {
    private Button btn_Tr;
    private Button btn_Co;
    private Button btn_Invite;
    private String user;
    private String res;
    private JSONObject invites;
    private TextView welcomeTextView;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.setTitle(getString(R.string.dashboard));

        user = getIntent().getStringExtra("USERNAME");

        btn_Tr = findViewById(R.id.training);
        btn_Co = findViewById(R.id.competitive);
        btn_Invite = findViewById(R.id.inviteRoom);
        welcomeTextView = findViewById(R.id.welcomeText);
        welcomeTextView.setText(getString(R.string.lets_take_a_quiz) + user + "!");

//       PopUpActivity.checkForInvites(user,Dashboard.this);

        checkForInvites();


        btn_Tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("MULTI", "0");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteRoom.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("MULTI", "0");
                startActivity(intent);
            }
        });

        btn_Co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompetitiveActivity.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("MULTI", "1");
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Item_Profile:
                // for view profile
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("USERNAME", user);
                startActivity(intent);
                return true;
            case R.id.Item_Friends:
                // for friends
                intent = new Intent(getApplicationContext(), FriendsRecyclerViewActivity.class);
                intent.putExtra("USERNAME", user);
                startActivity(intent);
                return true;
            case R.id.Item_QSuggestion:
                // for question suggestions
                intent = new Intent(getApplicationContext(), QFactoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.Item_Logout:
                // for logout
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            ActivityCompat.finishAffinity(Dashboard.this);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.double_back_click_msg, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void checkForInvites() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firsttry-272817.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        final JSONObject[] invites = new JSONObject[1];
        final String[] res = new String[1];
        Call<ResponseBody> mService = service.get_invites(user);
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
                        AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this)
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_info)
                                //set title
                                .setTitle("You\'ve got new invites!")
                                //set positive button
                                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what would happen when positive button is clicked
                                        Intent intent = new Intent(Dashboard.this, InviteRoom.class);
                                        intent.putExtra("USERNAME", user);
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

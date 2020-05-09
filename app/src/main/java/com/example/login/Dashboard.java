package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

        user = getIntent().getStringExtra("USERNAME");

        btn_Tr = findViewById(R.id.training);
        btn_Co = findViewById(R.id.competitive);
        btn_Invite = findViewById(R.id.inviteRoom);
        welcomeTextView = findViewById(R.id.welcomeText);
        welcomeTextView.setText("Let's take a quiz, " + user + "!");

//        PopUpInvite ana = new PopUpInvite();
//        ana.checkForInvites(user, Dashboard.this);
        PopUpActivity.checkForInvites(user,Dashboard.this);
        //checkForInvites();

        btn_Tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","0");
                startActivity(intent);
            }
        });

        btn_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteRoom.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","0");
                startActivity(intent);
            }
        });

        btn_Co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompetitiveActivity.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","1");
                startActivity(intent);
            }
        });

    }

    public void checkForInvites() {

        Call<ResponseBody> mService = service.get_invites(user);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                try {
                    res = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    invites = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.v("INVITESSS", invites.getString("no_rooms"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int no_rooms = 0;
                try {
                    no_rooms = Integer.parseInt(invites.getString("no_rooms"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(no_rooms > 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this)
                            //set icon
                        .setIcon(android.R.drawable.ic_dialog_info)
                        //set title
                        .setTitle(getResources().getString(R.string.view_invites_or_not))
                        //set positive button
                        .setPositiveButton("View", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                Intent intent = new Intent(getApplicationContext(), InviteRoom.class);
                                    intent.putExtra("USERNAME", user);
                                    intent.putExtra("MULTI", "1");
                                    startActivity(intent);
                                finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
//
                }else{
                    checkForInvites();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

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
//        Toast.makeText(getApplicationContext(),"HOME",Toast.LENGTH_LONG);
        switch (item.getItemId()) {
            case R.id.Item_Profile:
                // for view profile
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("USERNAME",user);
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
                // for view profile
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }



}

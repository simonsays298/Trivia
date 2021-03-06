package com.example.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrainingActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    UserService service = retrofit.create(UserService.class);

    private Button history;
    private Button geo;
    private Button arts;
    private Button movie;
    private Button science;
    private String user;
    private String multi;
    private String invite;
    private String invitedFriend;
    private String foundOpponent;
    private Button leaveRoom;
    private boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        user = getIntent().getStringExtra("USERNAME");
        multi = getIntent().getStringExtra("MULTI");
        invite = getIntent().getStringExtra("INVITE");
        if(getIntent().hasExtra("INVITED FRIEND") && getIntent().hasExtra("INVITE")){
            invitedFriend = getIntent().getStringExtra("INVITED FRIEND");
            invite = getIntent().getStringExtra("INVITE");
        }else{
            invite = "not invited";
        }

        //PopUpActivity.checkForInvites(user,TrainingActivity.this);
        //checkForInvites();

        history = findViewById(R.id.History);
        geo = findViewById(R.id.geography);
        arts = findViewById(R.id.Arts);
        movie = findViewById(R.id.Movies);
        science = findViewById(R.id.Science);
        leaveRoom = findViewById(R.id.leaveRoom);
        leaveRoom.setVisibility(View.GONE);




        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("username", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain", "2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("multi", multi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(invite.equals("invited")){
                    Log.v("INVITED ", invitedFriend);
                    try {
                        json.put("friend", invitedFriend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Call<ResponseBody> mService = service.new_game(json);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        assert response.body() != null;
                        String gamesId = null;
                        try {
                            gamesId = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.v("TAGUL", gamesId);
                        foundOpponent = gamesId;
                        if (multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", getResources().getString(R.string.historyT));
                            intent.putExtra("MULTI", multi);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            leaveRoom.setVisibility(View.VISIBLE);
                            waitForOpponent(gamesId, getResources().getString(R.string.historyT));
                            delOrNotGame(gamesId);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());

                    }
                });
            }
        });

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject json = new JSONObject();
                try {
                    json.put("username", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("multi", multi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(invite.equals("invited")){
                    Log.v("INVITED ", invitedFriend);
                    try {
                        json.put("friend", invitedFriend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Call<ResponseBody> mService = service.new_game(json);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        assert response.body() != null;
                        String gamesId = null;
                        try {
                            gamesId = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.v("TAGUL", gamesId);
                        foundOpponent = gamesId;
                        if (multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", getResources().getString(R.string.geo));
                            intent.putExtra("MULTI", multi);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            leaveRoom.setVisibility(View.VISIBLE);
                            waitForOpponent(gamesId, getResources().getString(R.string.geo));
                            delOrNotGame(gamesId);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());

                    }
                });
            }
        });

        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("username", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain", "5");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("multi", multi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(invite.equals("invited")){
                    Log.v("INVITED ", invitedFriend);
                    try {
                        json.put("friend", invitedFriend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Call<ResponseBody> mService = service.new_game(json);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        assert response.body() != null;
                        String gamesId = null;
                        try {
                            gamesId = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.v("TAGUL", gamesId);
                        foundOpponent = gamesId;
                        if (multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", getResources().getString(R.string.arts));
                            intent.putExtra("MULTI", multi);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            leaveRoom.setVisibility(View.VISIBLE);
                            waitForOpponent(gamesId, getResources().getString(R.string.arts));
                            delOrNotGame(gamesId);
//                            Log.v("TAGUL", "BRAVO MAI ASTEPATA");
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());

                    }
                });
            }
        });

        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("username", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain", "4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("multi", multi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(invite.equals("invited")){
                    Log.v("INVITED ", invitedFriend);
                    try {
                        json.put("friend", invitedFriend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                Call<ResponseBody> mService = service.new_game(json);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        assert response.body() != null;
                        String gamesId = null;
                        try {
                            gamesId = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.v("TAGUL", gamesId);
                        foundOpponent = gamesId;
                        if (multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", getResources().getString(R.string.movie));
                            intent.putExtra("MULTI", multi);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            leaveRoom.setVisibility(View.VISIBLE);
                            waitForOpponent(gamesId, getResources().getString(R.string.movie));
                            delOrNotGame(gamesId);
//                            Log.v("TAGUL", "BRAVO MAI ASTEPATA");
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());

                    }
                });
            }
        });

        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("username", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain", "3");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("multi", multi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(invite.equals("invited")){
                    Log.v("INVITED ", invitedFriend);
                    try {
                        json.put("friend", invitedFriend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Call<ResponseBody> mService = service.new_game(json);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        assert response.body() != null;
                        String gamesId = null;
                        try {
                            gamesId = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.v("TAGUL", gamesId);
                        foundOpponent = gamesId;
                        if (multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", getResources().getString(R.string.science));
                            intent.putExtra("MULTI", multi);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            leaveRoom.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.op_to_join), Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, getResources().getString(R.string.science));
                            delOrNotGame(gamesId);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());

                    }
                });
            }
        });

        leaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leavingRoom(foundOpponent);
            }
        });


    }

    private void delOrNotGame(String gamesId) {
        science.setEnabled(false);
        geo.setEnabled(false);
        movie.setEnabled(false);
        arts.setEnabled(false);
        history.setEnabled(false);
        showDiag(gamesId);
    }

    private void waitForOpponent(String gamesId, String topic) {

        if (!stop) {
            Call<ResponseBody> mService = service.found_opponent(foundOpponent);
            mService.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    assert response.body() != null;

                    try {
                        String res = response.body().string();
                        if (res.equals("Yes")) {

                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", topic);
                            intent.putExtra("MULTI", multi);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            if(res.equals("Declined")){
                                Toast.makeText(TrainingActivity.this, "Your invitation got declined", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                intent.putExtra("USERNAME", user);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                stop = true;

                            }else{
                                if (!stop)
                                    waitForOpponent(gamesId, topic);
                                else
                                    finish();
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("TAGUL", t.getMessage());

                }
            });
        } else {
            finish();
        }


    }

    private void showDiag(String gamesId) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_info)
                //set title
                .setTitle(getResources().getString(R.string.cancel_room_or_not))
                //set positive button
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        stop = true;
                        JSONObject json = new JSONObject();
                        try {
                            json.put("id", gamesId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Call<ResponseBody> mService = service.delete_game(json);
                        mService.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                assert response.body() != null;
                                Intent intent = new Intent(getApplicationContext(), CompetitiveActivity.class);
                                intent.putExtra("USERNAME", user);
                                intent.putExtra("MULTI", multi);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.v("TAGUL", t.getMessage());

                            }
                        });

                    }
                })
                //set negative button
                .setNegativeButton("Wait", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.looking_for_player), Toast.LENGTH_LONG).show();
                    }
                })
                .show();

    }

    private void leavingRoom(String gamesId){
        stop = true;
        JSONObject json = new JSONObject();
        try {
            json.put("id", gamesId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> mService = service.delete_game(json);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;

                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("MULTI", multi);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });
    }

}

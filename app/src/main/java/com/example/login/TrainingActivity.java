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
    private String foundOpponent;
    private boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        user = getIntent().getStringExtra("USERNAME");
        multi = getIntent().getStringExtra("MULTI");

        history = findViewById(R.id.History);
        geo = findViewById(R.id.geography);
        arts = findViewById(R.id.Arts);
        movie = findViewById(R.id.Movies);
        science = findViewById(R.id.Science);

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
                            startActivity(intent);
                        } else {
                            science.setEnabled(false);
                            geo.setEnabled(false);
                            movie.setEnabled(false);
                            arts.setEnabled(false);
                            history.setEnabled(false);
                            waitForOpponent(gamesId, getResources().getString(R.string.historyT));
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            //Log.v("TAGUL", "BRAVO MAI ASTEPATA");
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
                            startActivity(intent);
                        } else {
                            science.setEnabled(false);
                            geo.setEnabled(false);
                            movie.setEnabled(false);
                            arts.setEnabled(false);
                            history.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, getResources().getString(R.string.geo));
                            Log.v("TAGUL", "BRAVO MAI ASTEPATA");
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
                            startActivity(intent);
                        } else {
                            science.setEnabled(false);
                            geo.setEnabled(false);
                            movie.setEnabled(false);
                            arts.setEnabled(false);
                            history.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, getResources().getString(R.string.arts));
                            Log.v("TAGUL", "BRAVO MAI ASTEPATA");
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
                            startActivity(intent);
                        } else {
                            science.setEnabled(false);
                            geo.setEnabled(false);
                            movie.setEnabled(false);
                            arts.setEnabled(false);
                            history.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, getResources().getString(R.string.movie));
                            Log.v("TAGUL", "BRAVO MAI ASTEPATA");
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
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();

                            science.setEnabled(false);
                            geo.setEnabled(false);
                            movie.setEnabled(false);
                            arts.setEnabled(false);
                            history.setEnabled(false);
                            waitForOpponent(gamesId, getResources().getString(R.string.science));
                            showDiag(gamesId);

                            Log.v("TAGUL", "BRAVO MAI ASTEPATA");
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());

                    }
                });
            }
        });


    }

    private void waitForOpponent(String gamesId, String topic) {


        if (stop == false) {
            Call<ResponseBody> mService = service.found_opponent(foundOpponent);
            mService.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    assert response.body() != null;
                    try {
                        if (response.body().string().equals("Yes")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", topic);
                            intent.putExtra("MULTI", multi);
                            startActivity(intent);
                        } else {
                            if (stop == false)
                                waitForOpponent(gamesId, topic);
                            else
                                finish();
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
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Are you sure to Exit")
//set message
                .setMessage("Exiting will call finish() method")
//set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                        Call<ResponseBody> mService = service.delet_game(json);
                        mService.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                assert response.body() != null;
                                try {
                                    Log.v("RESPONSEE", response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //if(response.body().string().contains("Room deleted")) {
                                Intent intent = new Intent(getApplicationContext(), CompetitiveActivity.class);
                                intent.putExtra("USERNAME", user);
                                intent.putExtra("MULTI", multi);
                                startActivity(intent);
                                finish();
                                //}

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.v("TAGUL", t.getMessage());

                            }
                        });

                        finish();
                    }
                })
//set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        //waitForOpponent(gamesId, "SCIENCE");
                        Toast.makeText(getApplicationContext(), "Still Waiting", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

}

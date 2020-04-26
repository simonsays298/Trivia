package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class TrainingActivity extends AppCompatActivity {

    private Button history;
    private Button geo;
    private Button arts;
    private Button movie;
    private Button science;
    private String user;
    private String multi;
    private String foundOpponent;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);
    //SharedPReferences

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
                    json.put("username",user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain","2");
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

                        Log.v("TAGUL",gamesId);
                        foundOpponent = gamesId;
                        if(multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", "HISTORY");
                            intent.putExtra("MULTI",multi);
                            startActivity(intent);
                        }else{
                            history.setEnabled(false);
                            waitForOpponent(gamesId, "HISTORY");
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            Log.v("TAGUL","BRAVO MAI ASTEPATA");
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
                    json.put("username",user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain","1");
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

                        Log.v("TAGUL",gamesId);
                        foundOpponent = gamesId;
                        if(multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", "GEOGRAPHY");
                            intent.putExtra("MULTI",multi);
                            startActivity(intent);
                        }else{
                            geo.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, "GEOGRAPHY");
                            Log.v("TAGUL","BRAVO MAI ASTEPATA");
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
                    json.put("username",user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain","5");
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

                        Log.v("TAGUL",gamesId);
                        foundOpponent = gamesId;
                        if(multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", "ARTS & SPORTS");
                            intent.putExtra("MULTI",multi);
                            startActivity(intent);
                        }else{
                            arts.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, "ARTS & SPORTS");
                            Log.v("TAGUL","BRAVO MAI ASTEPATA");
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
                    json.put("username",user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain","4");
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

                        Log.v("TAGUL",gamesId);
                        foundOpponent = gamesId;
                        if(multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", "MOVIES & CELEBRITIES");
                            intent.putExtra("MULTI",multi);
                            startActivity(intent);
                        }else{
                            movie.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            waitForOpponent(gamesId, "MOVIES & CELEBRITIES");
                            Log.v("TAGUL","BRAVO MAI ASTEPATA");
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
                    json.put("username",user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("domain","3");
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

                        Log.v("TAGUL",gamesId);
                        foundOpponent = gamesId;
                        if(multi.equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", gamesId);
                            intent.putExtra("TOPIC", "SCIENCE");
                            intent.putExtra("MULTI",multi);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Wait for the opponent to join", Toast.LENGTH_LONG).show();
                            science.setEnabled(false);
                            waitForOpponent(gamesId, "SCIENCE");
                            Log.v("TAGUL","BRAVO MAI ASTEPATA");
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

    private void waitForOpponent(String gamesId, String topic){

        Call<ResponseBody> mService = service.found_opponent(foundOpponent);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                try {
                    if(response.body().string().equals("Yes")){
                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                        intent.putExtra("USERNAME", user);
                        intent.putExtra("GAMESID", gamesId);
                        intent.putExtra("TOPIC", topic);
                        intent.putExtra("MULTI",multi);
                        startActivity(intent);
                    }else{
                        waitForOpponent(gamesId,topic);
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
    }

}

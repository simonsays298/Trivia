package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class GameDone extends AppCompatActivity {

    Timer timer;
    private String user ;
    private String points;
    private String getId;
    private TextView messageGame;
    private TextView finalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_done);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firsttry-272817.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        messageGame = findViewById(R.id.messageG);
        finalScore = findViewById(R.id.finalScore);


        user = getIntent().getStringExtra("USERNAME");
        points = getIntent().getStringExtra("POINTS");
        getId = getIntent().getStringExtra("ID");

        finalScore.setText(points);

        if(Integer.parseInt(points) == 0){
            messageGame.setText("Better Luck Next Time!");
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject finish = new JSONObject();
                try {
                    finish.put("id", getId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    //Log.v("TAGUL", String.valueOf(points));
                    finish.put("points", points);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<ResponseBody> finishService = service.finish_game(finish);
                finishService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("USERNAME",user);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        },5000);




    }
}

package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    private TextView msgScor;
    private ImageView image;
    private String multi;
    TextView curUser, curScor;
    TextView opUser, opScor;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_done);


        messageGame = findViewById(R.id.messageG);
        finalScore = findViewById(R.id.finalScore);
        msgScor = findViewById(R.id.yourScore);
        image = findViewById(R.id.imageView);
        curUser = findViewById(R.id.curUser);
        curScor = findViewById(R.id.curScor);
        opUser = findViewById(R.id.opUser);
        opScor = findViewById(R.id.opScor);

        //msgScor.setVisibility(View.GONE);
        image.setVisibility(View.GONE);

        user = getIntent().getStringExtra("USERNAME");
        points = getIntent().getStringExtra("POINTS");
        getId = getIntent().getStringExtra("ID");
        multi = getIntent().getStringExtra("MULTI");




        msgScor.setVisibility(View.VISIBLE);
        finalScore.setText(points);




        if(multi.equals("0")) {


            if(Integer.parseInt(points) == 0){
                messageGame.setText(R.string.luck_next_time);
                image.setImageResource(R.drawable.ghost);
            }
            image.setVisibility(View.VISIBLE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Log.v("TAGUL","MULTI"+multi);
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
                        Log.v("TAGUL", getId);
                        finish.put("points", points);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        finish.put("username", user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ResponseBody> finishService = service.finish_game(finish);
                    finishService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                Log.v("TAGUL", response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.putExtra("USERNAME", user);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }, 1500);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }else{
            msgScor.setVisibility(View.VISIBLE);

            JSONObject finish = new JSONObject();
            try {
                finish.put("id", getId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.v("TAGUL", getId);
                finish.put("points", points);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                finish.put("username", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<ResponseBody> finishService = service.finish_game(finish);
            finishService.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        Log.v("TAGUL", response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                    intent.putExtra("USERNAME", user);
//                    startActivity(intent);
//                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            waitForopponent();

        }

    }
    public void waitForopponent(){

        Call<ResponseBody> finishService = service.get_winner(getId);
        finishService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String winner = "";
                JSONObject res = null;
                try {
                    winner = response.body().string();
                    res = new JSONObject(winner);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if(winner.contains("Game not done")){

                    messageGame.setText("Waiting fot the opponent to finish...");
                    image.setVisibility(View.GONE);
                    waitForopponent();
                }else{
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    Log.v("WINNER",winner);
                    try {
                        if(res.getString("winner").equals(user)){
                            messageGame.setText("Congratulations, you won!");
                        }else{
                            messageGame.setText(R.string.luck_next_time);
                            image.setImageResource(R.drawable.ghost);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        msgScor.setText("WINNER is "+ res.getString("winner"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        finalScore.setText(res.getString("points"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    curUser.setText("Your Score");
                    curScor.setText(points);
                    try {
                        opUser.setText(res.getString("winner"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        opScor.setText(res.getString("points"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    image.setVisibility(View.VISIBLE);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.putExtra("USERNAME", user);
                            startActivity(intent);
                            finish();
                        }
                    }, 5000);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

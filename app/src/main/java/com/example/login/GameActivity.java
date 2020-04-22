package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class GameActivity extends AppCompatActivity {

    private String getId;
    private String user;
    private String topicName;
    private TextView question;
    private TextView timer;
    private TextView topic;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private int counter = 0;
    private int points = 0;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

         getId = getIntent().getStringExtra("GAMESID");
         user = getIntent().getStringExtra("USERNAME");
         topicName = getIntent().getStringExtra("TOPIC");


        question = findViewById(R.id.Question);
        timer = findViewById(R.id.timer);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        topic = findViewById(R.id.topicName);
        timer.setText("" + 16);
        topic.setText(topicName);

        new CountDownTimer(15 * 1000, 1000){


            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(""+ millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(GameActivity.this, "Quiz Over", Toast.LENGTH_LONG).show();
            }
        }.start();

        loadQuestions();



    }
    public void loadQuestions(){
        JSONObject obj = null;
        Call<ResponseBody> mService = service.get_questions(getId);
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

                JSONObject obj = null;
                try {
                    obj = new JSONObject(gamesId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("TAGUL", obj.toString());
                try {
                    question.setText(obj.getString("question"));
                    ans1.setText(obj.getString("answer0"));
                    ans2.setText(obj.getString("answer1"));
                    ans3.setText(obj.getString("answer2"));
                    ans4.setText(obj.getString("answer3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject finalObj = obj;
                ans1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer1").equals(finalObj.getString("right"))){
                                Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(counter < 5){
                            counter++;
                            loadQuestions();
                        }else{
                            Toast.makeText(getApplicationContext(), "FINISH GAME", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ans2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer2").equals(finalObj.getString("right"))){
                                Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(counter < 5){
                            counter++;
                            loadQuestions();
                        }else{
                            Toast.makeText(getApplicationContext(), "FINISH GAME", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                ans3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer3").equals(finalObj.getString("right"))){
                                Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(counter < 5){
                            counter++;
                            loadQuestions();
                        }else{
                            Toast.makeText(getApplicationContext(), "FINISH GAME", Toast.LENGTH_SHORT).show();
                        }

                        }
                });
                ans4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer4").equals(finalObj.getString("right"))){
                                Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(counter < 5){
                            counter++;
                            loadQuestions();
                        }else{
                            Toast.makeText(getApplicationContext(), "FINISH GAME", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });

        JSONObject finish = new JSONObject();
        try {
            finish.put("id",getId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            finish.put("points", String.valueOf(points));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (counter == 5){
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
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }
}

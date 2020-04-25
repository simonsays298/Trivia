package com.example.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;

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
    private TextView myPoints;
    private TextView topic;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private TextView checkAns;
    private int counter = 0;
    private int points = 0;
    private CountDownTimer tm = null;


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
        myPoints = findViewById(R.id.myPoints);
        checkAns = findViewById(R.id.checkAnswer);

        topic.setText(topicName);
        myPoints.setText("Points: "+ points);

        tm = new CountDownTimer(16 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
                
                if(millisUntilFinished / 1000 == 0){
                    tm.cancel();
                    counter +=1;
                    loadQuestions();
                }
                if(millisUntilFinished / 1000 <= 5){
                    checkAns.setText("Better Hurry!!!");
                    checkAns.setTextColor(Color.RED);
                    checkAns.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    timer.setTextColor(Color.RED);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                }else{
                    timer.setTextColor(Color.GRAY);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    checkAns.setText("");
                }


            }

            @Override
            public void onFinish() {
                
            }
        };

        loadQuestions();


    }

    public void loadQuestions() {

        timer.setText("" + 16);

        if (tm != null) {
            tm.start();
        }


        Call<ResponseBody> mService = service.get_questions(getId, user);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;

                String gamesId = null;
                JSONObject resGet = null;
                try {
                    gamesId = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    resGet = new JSONObject(gamesId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("TAGUL",resGet.toString());

                if(counter < 5) {
                    try {
                        question.setText(String.format("%d. %s", counter + 1, resGet.getString("question")));
                        ans1.setText(resGet.getString("answer0"));
                        ans2.setText(resGet.getString("answer1"));
                        ans3.setText(resGet.getString("answer2"));
                        ans4.setText(resGet.getString("answer3"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject finalObj = resGet;
                ans1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer0").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                TransitionDrawable trans = correctAns();
                                ans1.setBackground(trans);
                                trans.startTransition(400);

                                points = points + 10;
                                myPoints.setText("Points: " + points);
                            } else {
                                TransitionDrawable trans = wrongAns();
                                ans1.setBackground(trans);
                                trans.startTransition(400);

                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: " + points);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
                            loadQuestions();
                        }else{
                            tm.cancel();
                            finish();
                            finishGame();
                        }
                    }
                }

                );

                ans2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer1").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                TransitionDrawable trans = correctAns();
                                ans2.setBackground(trans);
                                trans.startTransition(400);

                                points = points + 10;
                                myPoints.setText("Points: "+ points);
                            } else {
                                TransitionDrawable trans = wrongAns();
                                ans2.setBackground(trans);
                                trans.startTransition(400);

                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: "+ points);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
                            loadQuestions();
                        }else{
                            tm.cancel();
                            finish();
                            finishGame();
                        }

                    }
                });

                ans3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer2").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                TransitionDrawable trans = correctAns();
                                ans3.setBackground(trans);
                                trans.startTransition(400);

                                points = points + 10;
                                myPoints.setText("Points: "+ points);
                            } else {
                                TransitionDrawable trans = wrongAns();
                                ans3.setBackground(trans);
                                trans.startTransition(400);

                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: "+ points);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
                            loadQuestions();
                        }else{
                            tm.cancel();
                            finish();
                            finishGame();
                        }

                    }
                });
                ans4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (finalObj.getString("answer3").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                TransitionDrawable trans = correctAns();
                                ans4.setBackground(trans);
                                trans.startTransition(400);

                                points = points + 10;
                                myPoints.setText("Points: "+ points);
                            } else {
                                TransitionDrawable trans = wrongAns();
                                ans4.setBackground(trans);
                                trans.startTransition(400);

                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: "+ points);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
                            loadQuestions();
                        }else{
                            tm.cancel();
                            finish();
                            finishGame();
                        }
                    }

                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());
            }
        });


        if (counter == 5) {
            tm.cancel();
            finishGame();
        }



    }

    public void finishGame() {

                Intent intent = new Intent(getApplicationContext(), GameDone.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("ID",getId);
                intent.putExtra("POINTS",String.valueOf(points));
                startActivity(intent);
                finish();
    }

    public TransitionDrawable correctAns(){
        ColorDrawable[] color = {new ColorDrawable(Color.GREEN), new ColorDrawable(Color.LTGRAY)};
        TransitionDrawable trans = new TransitionDrawable(color);
        return trans;
    }
    public TransitionDrawable wrongAns(){
        ColorDrawable[] color = {new ColorDrawable(Color.RED), new ColorDrawable(Color.LTGRAY)};
        TransitionDrawable trans = new TransitionDrawable(color);
        return trans;
    }

}






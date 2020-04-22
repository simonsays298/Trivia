package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer timing;

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
        myPoints.setText("Points: "+ String.valueOf(points));
        tm = new CountDownTimer(16 * 1000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                Toast.makeText(GameActivity.this, "Quiz Over", Toast.LENGTH_LONG).show();
            }
        };



        loadQuestions();







    }

    public void loadQuestions() {



        timer.setText("" + 16);
        //checkAns.setText("");

        if (tm != null) {
            tm.start();
        }



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


                //Log.v("TAGUL", obj.toString());
                if(counter != 5) {
                    try {
                        question.setText(String.valueOf(counter + 1) + "." + " " + obj.getString("question"));
                        ans1.setText(obj.getString("answer0"));
                        ans2.setText(obj.getString("answer1"));
                        ans3.setText(obj.getString("answer2"));
                        ans4.setText(obj.getString("answer3"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject finalObj = obj;
                ans1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ans3.setBackgroundColor(Color.LTGRAY);
//                        ans1.setBackgroundColor(Color.LTGRAY);
//                        ans2.setBackgroundColor(Color.LTGRAY);
//                        ans4.setBackgroundColor(Color.LTGRAY);
                        try {

                            if (finalObj.getString("answer0").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                //Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_LONG).show();
                                //Log.v("TAGUL", "Correct");
                                // Wrong option
                                //ans1.setBackgroundColor(Color.GREEN);
                                checkAns.setText("CORRECT");
                                checkAns.setTextColor(Color.GREEN);




                                points = points + 10;
                                myPoints.setText("Points: " + String.valueOf(points));
                            } else {
                                checkAns.setText("WRONG");
                                checkAns.setTextColor(Color.RED);

                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: " + String.valueOf(points));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();

                            counter++;

                            loadQuestions();

                        }
                    }
                }

                );

                ans2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        ans3.setBackgroundColor(Color.LTGRAY);
//                        ans1.setBackgroundColor(Color.LTGRAY);
//                        ans2.setBackgroundColor(Color.LTGRAY);
//                        ans4.setBackgroundColor(Color.LTGRAY);
                        try {
                            if (finalObj.getString("answer0").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                //ans2.setBackgroundColor(Color.GREEN);
                                checkAns.setText("CORRECT");
                                checkAns.setTextColor(Color.GREEN);
                                points = points + 10;
                                myPoints.setText("Points: "+ String.valueOf(points));
                            } else {
                                //ans2.setBackgroundColor(Color.RED);
                                checkAns.setText("WRONG");
                                checkAns.setTextColor(Color.RED);
                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: "+ String.valueOf(points));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
//                            SystemClock.sleep(1000);
                            loadQuestions();
                        }

                    }
                });
                ans3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ans3.setBackgroundColor(Color.LTGRAY);
//                        ans1.setBackgroundColor(Color.LTGRAY);
//                        ans2.setBackgroundColor(Color.LTGRAY);
//                        ans4.setBackgroundColor(Color.LTGRAY);
                        try {
                            if (finalObj.getString("answer2").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                                //ans3.setBackgroundColor(Color.GREEN);
                                checkAns.setText("CORRECT");
                                checkAns.setTextColor(Color.GREEN);
                                points = points + 10;
                                myPoints.setText("Points: "+ String.valueOf(points));
                            } else {
                                //ans3.setBackgroundColor(Color.RED);
                                checkAns.setText("WRONG");
                                checkAns.setTextColor(Color.RED);
                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: "+ String.valueOf(points));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
//                            SystemClock.sleep(1000);

                            loadQuestions();
                        }

                    }
                });
                ans4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //ans4.setBackgroundColor(Color.LTGRAY);
                        try {
                            if (finalObj.getString("answer3").toLowerCase().contains(finalObj.getString("right").toLowerCase())) {
                               // ans4.setBackgroundColor(Color.GREEN);
                                checkAns.setText("CORRECT");
                                checkAns.setTextColor(Color.GREEN);
                                points = points + 10;
                                myPoints.setText("Points: "+ String.valueOf(points));
                            } else {
                                //ans4.setBackgroundColor(Color.RED);
                                checkAns.setText("WRONG");
                                checkAns.setTextColor(Color.RED);
                                if (points != 0) {
                                    points = points - 5;
                                    myPoints.setText("Points: "+ String.valueOf(points));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (counter < 5) {
                            tm.cancel();
                            counter++;
//                            SystemClock.sleep(1000);
                            loadQuestions();
                            //ans4.setBackgroundColor(Color.LTGRAY);
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

//
    }

}






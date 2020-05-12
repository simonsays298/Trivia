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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
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
    HashMap<String, ArrayList<String>> gameQuestions = new HashMap<String, ArrayList<String>>();
    JSONObject resGet;
    String gamesId;
    String multi;
    private int endgame;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(getIntent().hasExtra("GAMESID") && getIntent().hasExtra("USERNAME")
        && getIntent().hasExtra("TOPIC") && getIntent().hasExtra("MULTI")){
            getId = getIntent().getStringExtra("GAMESID");
            user = getIntent().getStringExtra("USERNAME");
            topicName = getIntent().getStringExtra("TOPIC");
            multi = getIntent().getStringExtra("MULTI");
        }


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
        myPoints.setText("Points: " + points);

        tm = new CountDownTimer(16 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);

                if (millisUntilFinished / 1000 == 0) {
                    tm.cancel();
                    counter += 1;
                    if (counter == 5) {
                        tm.cancel();
                        finish();
                        finishGame();
                    }

                    try {
                        getQuestions(counter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (millisUntilFinished / 1000 <= 5) {
                    checkAns.setVisibility(View.VISIBLE);
                    checkAns.setText(R.string.hurry_up);
                    checkAns.setTextColor(Color.RED);
                    checkAns.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    timer.setTextColor(Color.RED);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    timer.setTextColor(Color.GRAY);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    checkAns.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFinish() {

            }
        };

        Call<ResponseBody> mService = service.get_questions(getId);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
                resGet = null;
                try {
                    gamesId = response.body().string();
                    resGet = new JSONObject(gamesId);
                    getQuestions(counter);
                   // Log.v("TAGUL", gamesId);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());
            }
        });

    }

    public void getQuestions(int i) throws JSONException {

        JSONObject qAndA = resGet.getJSONObject(String.valueOf(i));
        ArrayList<String> list = new ArrayList<>();
        list.add(qAndA.getString("question"));
        list.add(qAndA.getString("answer0"));
        list.add(qAndA.getString("answer1"));
        list.add(qAndA.getString("answer2"));
        list.add(qAndA.getString("answer3"));
        list.add(qAndA.getString("right"));
        gameQuestions.put(String.valueOf(i), list);
        loadQuestions(i);


    }

    public void loadQuestions(int i) throws JSONException {
        timer.setText("" + 16);

        if (counter == 5) {
            tm.cancel();
            finishGame();
        }

        if (tm != null) {
            tm.start();
        }

        String answ1 = Objects.requireNonNull(gameQuestions.get(String.valueOf(i))).get(1);
        String answ2 = Objects.requireNonNull(gameQuestions.get(String.valueOf(i))).get(2);
        String answ3 = Objects.requireNonNull(gameQuestions.get(String.valueOf(i))).get(3);
        String answ4 = Objects.requireNonNull(gameQuestions.get(String.valueOf(i))).get(4);
        String right = Objects.requireNonNull(gameQuestions.get(String.valueOf(i))).get(5);

        question.setText(String.format("%d. %s", i + 1, gameQuestions.get(String.valueOf(i)).get(0)));
        ans1.setText(answ1);
        ans2.setText(answ2);
        ans3.setText(answ3);
        ans4.setText(answ4);

        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answ1.toLowerCase().contains(right.toLowerCase())) {
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
                nextQuestion();

            }
        }
        );

        ans2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (answ2.toLowerCase().contains(right.toLowerCase())) {
                    TransitionDrawable trans = correctAns();
                    ans2.setBackground(trans);
                    trans.startTransition(400);

                    points = points + 10;
                    myPoints.setText("Points: " + points);
                } else {
                    TransitionDrawable trans = wrongAns();
                    ans2.setBackground(trans);
                    trans.startTransition(400);

                    if (points != 0) {
                        points = points - 5;
                        myPoints.setText("Points: " + points);
                    }
                }
                nextQuestion();
            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answ3.toLowerCase().contains(right.toLowerCase())) {
                    TransitionDrawable trans = correctAns();
                    ans3.setBackground(trans);
                    trans.startTransition(400);

                    points = points + 10;
                    myPoints.setText("Points: " + points);
                } else {
                    TransitionDrawable trans = wrongAns();
                    ans3.setBackground(trans);
                    trans.startTransition(400);

                    if (points != 0) {
                        points = points - 5;
                        myPoints.setText("Points: " + points);
                    }
                }
                nextQuestion();
            }
        });
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answ4.toLowerCase().contains(right.toLowerCase())) {
                    TransitionDrawable trans = correctAns();
                    ans4.setBackground(trans);
                    trans.startTransition(400);


                    points = points + 10;
                    myPoints.setText("Points: " + points);
                } else {
                    TransitionDrawable trans = wrongAns();
                    ans4.setBackground(trans);
                    trans.startTransition(400);

                    if (points != 0) {
                        points = points - 5;
                        myPoints.setText("Points: " + points);
                    }
                }
                nextQuestion();

            }

        });
    }

    private void nextQuestion() {
        tm.cancel();
        counter++;
        if (counter == 5) {
            tm.cancel();
            finishGame();
        }
        try {
            getQuestions(counter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void finishGame() {

        Intent intent = new Intent(getApplicationContext(), GameDone.class);
        intent.putExtra("USERNAME", user);
        intent.putExtra("ID", getId);
        intent.putExtra("POINTS", String.valueOf(points));
        intent.putExtra("MULTI", multi);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
    }

    public TransitionDrawable correctAns() {
        ColorDrawable[] color = {new ColorDrawable(Color.GREEN), new ColorDrawable(Color.LTGRAY)};
        TransitionDrawable trans = new TransitionDrawable(color);
        return trans;
    }

    public TransitionDrawable wrongAns() {
        ColorDrawable[] color = {new ColorDrawable(Color.RED), new ColorDrawable(Color.LTGRAY)};
        TransitionDrawable trans = new TransitionDrawable(color);
        return trans;
    }

}






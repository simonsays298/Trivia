package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TrainingActivity extends AppCompatActivity {

    private TextView my_question;
    private Button my_ans1;
    private Button my_ans2;
    private Button my_ans3;
    private Button my_ans4;
    private String rightAns;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        my_question = findViewById(R.id.question);
        my_ans1 = findViewById(R.id.ans1);
        my_ans2 = findViewById(R.id.ans2);
        my_ans3 = findViewById(R.id.ans3);
        my_ans4 = findViewById(R.id.ans4);

        my_question.setText("Care este capitala Romaniei?");
        my_ans1.setText("Bucuresti");
        my_ans2.setText("Londra");
        my_ans3.setText("Helsinki");
        my_ans4.setText("Roma");


    }
}

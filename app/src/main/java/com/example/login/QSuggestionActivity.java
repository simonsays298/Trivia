package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QSuggestionActivity extends AppCompatActivity {
    private CheckBox historyCheckBox;
    private CheckBox geographyCheckBox;
    private CheckBox scienceCheckBox;
    private CheckBox artsSportsCheckBox;
    private CheckBox moviesCheckBox;

    private EditText questionSuggestionText;
    private EditText rightAnswerText;
    private EditText wrongAnswer1Text;
    private EditText wrongAnswer2Text;
    private EditText wrongAnswer3Text;

    private Button sendButton;

    private String result;

    private JSONObject obj = new JSONObject();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_suggestion);
        Intent intent = getIntent();

        historyCheckBox = findViewById(R.id.history);
        geographyCheckBox = findViewById(R.id.geography);
        scienceCheckBox = findViewById(R.id.science);
        artsSportsCheckBox = findViewById(R.id.artsports);
        moviesCheckBox = findViewById(R.id.movies);

        questionSuggestionText = findViewById(R.id.questionSuggestion);
        rightAnswerText = findViewById(R.id.rightAnswerSuggestion);
        wrongAnswer1Text = findViewById(R.id.wrongAnswer1);
        wrongAnswer2Text = findViewById(R.id.wrongAnswer2);
        wrongAnswer3Text = findViewById(R.id.wrongAnswer3);

        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int domainID = 0;
            String suggestedQuestion = questionSuggestionText.getText().toString();
            String rightAnswerString = rightAnswerText.getText().toString();
            String wrongAnswer1String = wrongAnswer1Text.getText().toString();
            String wrongAnswer2String = wrongAnswer2Text.getText().toString();
            String wrongAnswer3String = wrongAnswer3Text.getText().toString();

            if (geographyCheckBox.isChecked()) domainID = 1;
            if (historyCheckBox.isChecked()) domainID = 2;
            if (scienceCheckBox.isChecked()) domainID = 3;
            if (moviesCheckBox.isChecked()) domainID = 4;
            if (artsSportsCheckBox.isChecked()) domainID = 5;


            result = "{\"question\":\"" + suggestedQuestion + "\",\"answer0\":\"" + wrongAnswer1String
                    + "\",\"answer1\":\"" + wrongAnswer2String +  "\",\"answer2\":\"" + wrongAnswer3String
                    + "\",\"right\":\"" + rightAnswerString + "\",\"domain\":\"" + domainID + "\"}";

            Call<ResponseBody> mService = service.createUser(result);

            mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getApplicationContext(), R.string.createQA, Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());
                    }
                });

            }

        });








    }
}

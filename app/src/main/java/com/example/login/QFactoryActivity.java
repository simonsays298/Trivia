package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QFactoryActivity extends AppCompatActivity {
    private Button suggestQButton;
    private Button rateQButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_factory);
        this.setTitle(getString(R.string.question_factory));

        suggestQButton = findViewById(R.id.suggestQButton);

        suggestQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QSuggestionActivity.class);
                startActivity(intent);
            }
        });

        rateQButton = findViewById(R.id.rateQButton);

        rateQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RateQuestionActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private Button btn_Tr;
    private Button btn_Co;
    private String user;
    private TextView welcomeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        user = getIntent().getStringExtra("USERNAME");

        btn_Tr = findViewById(R.id.training);
        btn_Co = findViewById(R.id.competitive);
        welcomeTextView = findViewById(R.id.welcomeText);
        welcomeTextView.setText("Let's take a quiz, " + user + "!");

        btn_Tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","0");
                startActivity(intent);
            }
        });

        btn_Co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompetitiveActivity.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","1");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
//        Toast.makeText(getApplicationContext(),"HOME",Toast.LENGTH_LONG);
        switch (item.getItemId()) {
            case R.id.Item_Profile:
                // for view profile
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("USERNAME",user);
                startActivity(intent);
                return true;
            case R.id.Item_Friends:
                // for friends
                intent = new Intent(getApplicationContext(), FriendsRecyclerViewActivity.class);
                intent.putExtra("USERNAME", user);
                startActivity(intent);
                return true;
            case R.id.Item_QSuggestion:
                // for question suggestions
                intent = new Intent(getApplicationContext(), QFactoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.Item_Logout:
                // for view profile
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

//    private void fooThis() {
//        Toast.makeText(getApplicationContext(),"HOME",Toast.LENGTH_LONG);
//    }


}

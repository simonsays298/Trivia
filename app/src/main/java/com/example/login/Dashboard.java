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
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    private Button btn_Tr;
    private Button btn_Co;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();

        btn_Tr = findViewById(R.id.training);
        btn_Co = findViewById(R.id.competitive);

        btn_Tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
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
                startActivity(intent);
                return true;
            case R.id.Item_Friends:
                // for friends
                intent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
                return true;
            case R.id.Item_QSuggestion:
                // for question suggestions
                intent = new Intent(getApplicationContext(), QSuggestionActivity.class);
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

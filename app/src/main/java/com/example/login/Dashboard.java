package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Dashboard extends AppCompatActivity {
    private Button btn_Tr;
    private Button btn_Co;
    private Button btn_Invite;
    private String user;
    private String res;
    private JSONObject invites;
    private TextView welcomeTextView;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.setTitle(getString(R.string.dashboard));

        user = getIntent().getStringExtra("USERNAME");

        btn_Tr = findViewById(R.id.training);
        btn_Co = findViewById(R.id.competitive);
        btn_Invite = findViewById(R.id.inviteRoom);
        welcomeTextView = findViewById(R.id.welcomeText);
        welcomeTextView.setText("Let's take a quiz, " + user + "!");

//       PopUpActivity.checkForInvites(user,Dashboard.this);

        btn_Tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","0");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteRoom.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","0");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_Co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompetitiveActivity.class);
                intent.putExtra("USERNAME",user);
                intent.putExtra("MULTI","1");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            ActivityCompat.finishAffinity(Dashboard.this);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.double_back_click_msg, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}

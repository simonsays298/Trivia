package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private EditText editText;
    private Button btn;
    private Button btnR;
    private EditText psswd;
    private String user;
    private String password;
    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    100);
        } else {
            Log.v("TAGUL", "ALREADY GRANTED");

        }

        btn = findViewById(R.id.Login);
        editText = findViewById(R.id.User);
        btnR = findViewById(R.id.buttonRegister);
        psswd = findViewById(R.id.Password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firsttry-272817.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make a request to server using retrofit

                user = editText.getText().toString();
                password = psswd.getText().toString();
                //result = "{\"username\":\"" + user + "\",\"password\":\"" + password +"\"}";

                Call<ResponseBody> mService = service.authenticate(user, password);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        assert response.body() != null;
                        try {
//                            Log.v("TAGUL", response.body().string());
                            String atext = response.body().string();
                            Log.v("TAGUL", Boolean.toString(atext.contains("not found")));
                            if (atext.contains("not found")) {
                                Toast.makeText(getApplicationContext(), "User/Password incorrect", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                intent.putExtra("USERNAME",user);
                                startActivity(intent);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("TAGUL", t.getMessage());
                    }
                });
            }
        });


        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make a request to server using retrofit
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }

        });


    }



}


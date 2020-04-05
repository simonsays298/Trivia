package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.internal.$Gson$Types;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTxt;
    private Button btnCreate;
    private String text;
    private JSONObject obj = new JSONObject();
    private String result;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Intent intent = getIntent();

        editTxt = findViewById(R.id.editText);
        btnCreate = findViewById(R.id.buttonCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = editTxt.getText().toString();

                result = "{\"username\":\"" + text + "\",\"points\":\"0\"}";


                Call<ResponseBody> mService = service.createUser(result);
                mService.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        assert response.body() != null;
                        try {
                            String atext = response.body().string();
                            if(atext.contains("already exists")){
                                Toast.makeText(getApplicationContext(), "The user already exists", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "You have succesfully created you account", Toast.LENGTH_LONG).show();
                                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
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



    }
}

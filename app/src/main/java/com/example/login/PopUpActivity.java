package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopUpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);


    }

    public static  void checkForInvites(String user, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firsttry-272817.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        final JSONObject[] invites = new JSONObject[1];
        final String[] res = new String[1];
        Call<ResponseBody> mService = service.get_invites(user);
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                invites[0] = null;
                res[0] = null;
                try {
                    res[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    invites[0] = new JSONObject(res[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.v("INVITESSS", invites[0].getString("no_rooms"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int no_rooms = 0;
                try {
                    no_rooms = Integer.parseInt(invites[0].getString("no_rooms"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(no_rooms > 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            //set icon
                            .setIcon(android.R.drawable.ic_dialog_info)
                            //set title
                            .setTitle("You\'ve got new invites!")
                            //set positive button
                            .setPositiveButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    Intent intent = new Intent(context, InviteRoom.class);
                                    intent.putExtra("USERNAME", user);
                                    intent.putExtra("MULTI", "1");
                                    context.startActivity(intent);
//                                    finish();
                                }
                            })
                            //set negative button
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
//
                }else{
                    checkForInvites(user,context);
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });


    }
}

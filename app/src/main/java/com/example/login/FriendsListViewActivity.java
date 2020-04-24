package com.example.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FriendsListViewActivity extends AppCompatActivity {
    private static final String TAG = "ListViewActivity";

    private FriendsArrayAdapter friendsArrayAdapter;
    private ListView listView;

    private static int colorIndex;

    private Button addNewFriendButton;
    final Context context = this;

    EditText friendNameText;

    String userName;
    String friendName;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_listview_layout);
        userName = getIntent().getStringExtra("USERNAME");

        colorIndex = 0;

        listView = (ListView) findViewById(R.id.listView);

        friendsArrayAdapter = new FriendsArrayAdapter(getApplicationContext(),
                R.layout.activity_listview_row_layout);
        listView.setAdapter(friendsArrayAdapter);

        Call<ResponseBody> mService1 = service.get_friends(userName);

        mService1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                String friendNameAndPoints = null;
                try {
                    friendNameAndPoints = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject obj = null;
                try {
                    assert friendNameAndPoints != null;
                    obj = new JSONObject(friendNameAndPoints);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int jsonLength = obj.length();

                for (int i = 0; i < jsonLength / 2; i++) {
                    try {
                        String friendN = obj.getString(String.valueOf(i));
                        String nrFriendPoints = obj.getString("points" + String.valueOf(i))
                                + " " + getString(R.string.points_friendslist);

                        FriendData friend = new FriendData(friendN, nrFriendPoints);
                        friendsArrayAdapter.add(friend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("FAIL Friends List View", t.getMessage());

            }
        });

        addNewFriendButton = findViewById(R.id.addNewFriendButton);
        addNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_add_new_friend);

                // set the custom dialog components - text and button
                friendNameText = (EditText) dialog.findViewById(R.id.friendToBeAdded);

                Button dialogOKButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

                // if OK button is clicked, type friend name and close the dialog
                dialogOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendName = friendNameText.getText().toString();
                        // check if friend name is not empty
                        if (friendName.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Friend name is empty", Toast.LENGTH_LONG).show();
                        } else {
                            String result = "{\"username\":\"" + userName + "\",\"friend\":\"" + friendName + "\"}";

                            Call<ResponseBody> mService = service.add_friend(result);
                            mService.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    assert response.body() != null;
                                    try {
                                        String atext = response.body().string();
                                        Log.v("Friend not user", Boolean.toString(atext.contains("is not a user")));

                                        // check if name is a user from DB
                                        if (atext.contains("is not a user")) {
                                            Toast.makeText(getApplicationContext(), "This is not a Trivia User!", Toast.LENGTH_LONG).show();
                                        } else if (atext.contains("Friend added")) {
                                            Toast.makeText(getApplicationContext(), R.string.friend_succ_added, Toast.LENGTH_LONG).show();
                                            friendsArrayAdapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.v("Fail friend name", t.getMessage());
                                }
                            });

                        }
                    }
                });

                dialog.show();
            }
        });


    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        refreshData();
//    }
}

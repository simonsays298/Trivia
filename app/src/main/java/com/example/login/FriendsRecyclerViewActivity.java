package com.example.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class FriendsRecyclerViewActivity extends AppCompatActivity {
    private static final String TAG = "ListViewActivity";

    List<FriendData> friendDataList = new ArrayList<>();

    private FriendDataAdapter friendDataAdapter;

    SwipeController swipeController = null;

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
        setContentView(R.layout.friends_recyclerview_layout);
        userName = getIntent().getStringExtra("USERNAME");

        friendDataAdapter = new FriendDataAdapter(friendDataList);
        setupRecyclerView();

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
                        friendDataList.add(i, friend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.v("SIZE", String.valueOf(friendDataList.size()));
                friendDataAdapter.notifyDataSetChanged();


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
                Button dialogCancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);

                dialogCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // if OK button is clicked, type friend name and close the dialog
                dialogOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendName = friendNameText.getText().toString();
                        // check if friend name is not empty
                        if (friendName.length() == 0) {
                            Toast.makeText(getApplicationContext(), R.string.friend_name_empty,
                                    Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(getApplicationContext(),
                                                    R.string.not_trivia_user, Toast.LENGTH_LONG).show();
                                        } else if (atext.contains("Friend added")) {
                                            Toast.makeText(getApplicationContext(),
                                                    R.string.friend_succ_added, Toast.LENGTH_LONG).show();
                                            FriendsRecyclerViewActivity.this.recreate();
                                            friendDataAdapter.notifyDataSetChanged();
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

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.listView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(friendDataAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                String friendRightSwiped = friendDataList.get(position).getFriendName();

                showRemoveAlertDialog(friendRightSwiped);
            }

            @Override
            public void onLeftClicked(int position) {
                String friendLeftSwiped = friendDataList.get(position).getFriendName();

            }

        });


        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void showRemoveAlertDialog(String friendRightSwiped) {
        AlertDialog alertDialog = new AlertDialog.Builder(FriendsRecyclerViewActivity.this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle(getString(R.string.sure_delete_friend) + " " + friendRightSwiped + " from list?")
                //set positive button
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked

                        String result = "{\"username\":\"" + userName + "\",\"friend\":\"" + friendRightSwiped + "\"}";

                        Call<ResponseBody> mService = service.delete_friend(result);
                        mService.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                assert response.body() != null;
                                try {
                                    String atext = response.body().string();
                                    Log.v("Friend deleted", Boolean.toString(atext.contains("Friend deleted")));
                                    if (atext.contains("Friend deleted")) {
                                        Toast.makeText(getApplicationContext(),
                                                R.string.friend_succ_del, Toast.LENGTH_LONG).show();
                                        FriendsRecyclerViewActivity.this.recreate();
                                        friendDataAdapter.notifyDataSetChanged();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.v("DELETE Friend", t.getMessage());
                            }
                        });

                        //finish();
                    }
                })

                //set negative button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

}

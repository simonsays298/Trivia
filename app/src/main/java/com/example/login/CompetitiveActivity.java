package com.example.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompetitiveActivity extends AppCompatActivity {



    private ArrayList<RoomData> exampleList;
    private Button createRoom;
    private String user;
    private String multi;
    JSONObject rooms;
    List<String> roomsList;
    HashMap<String, ArrayList<String>> nameIdMap;
    private RecyclerView mRecyclerView;
    private RoomAdapterActivity mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int noRooms;
    private int counter = 0;

    String res;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitive);

        user = getIntent().getStringExtra("USERNAME");
        multi = getIntent().getStringExtra("MULTI");
        checkForInvites();

        createRoom = findViewById(R.id.createRoom);



        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom.setText("Creating Room");
                createRoom.setEnabled(false);

                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("MULTI", multi);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
        createRoom.setEnabled(true);

        try {
            loadRooms();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void loadRooms() throws JSONException {

        Call<ResponseBody> mService = service.get_rooms(user);
        mService.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                res = null;
                rooms = null;

                try {
                    res = response.body().string();

                    rooms = new JSONObject(res);
                    nameIdMap = new HashMap<String, ArrayList<String>>();
                    exampleList = new ArrayList<RoomData>();
                    showList();

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showList() throws JSONException {
        noRooms = Integer.parseInt(rooms.getString("no_rooms"));
        if (noRooms > 0) {
            for (int i = 0; i < noRooms; i++) {
                JSONObject nameRoom = rooms.getJSONObject(String.valueOf(i));
                if (!exampleList.stream().anyMatch(o -> o.getText2().equals(nameRoom))) {
                    String name = nameRoom.getString("creator");
                    String id = nameRoom.getString("id");
                    String domain = nameRoom.getString("domain");
                    ArrayList<String> list = new ArrayList<>();
                    list.add(id);
                    list.add(domain);
                    exampleList.add(new RoomData(R.drawable.room_multi,"For Anyone", name));
//                    Log.v("ROOM",exampleList.get(0).getText2());
                    nameIdMap.put(name, list);
                }

            }
        }



        mRecyclerView = findViewById(R.id.rooms);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RoomAdapterActivity(exampleList, user, nameIdMap, multi, getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    loadRooms();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }
    public void checkForInvites() {
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

                int no_rooms = 0;
                try {
                    no_rooms = Integer.parseInt(invites[0].getString("no_rooms"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (no_rooms > 0) {

                    try {
                        AlertDialog alertDialog = new AlertDialog.Builder(CompetitiveActivity.this)
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_info)
                                //set title
                                .setTitle("You\'ve got new invites!")
                                //set positive button
                                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what would happen when positive button is clicked
                                        Intent intent = new Intent(CompetitiveActivity.this, InviteRoom.class);
                                        intent.putExtra("USERNAME", user);
                                        intent.putExtra("MULTI", "1");
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        dialogInterface.dismiss();

                                    }
                                })
                                //set negative button
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    } catch (WindowManager.BadTokenException ex) {
                        ex.printStackTrace();
                    }
//
                } else {


                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            checkForInvites();
                        }
                    }, 3000);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });


    }


}

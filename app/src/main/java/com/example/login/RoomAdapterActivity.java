package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RoomAdapterActivity extends RecyclerView.Adapter<RoomAdapterActivity.ExampleViewHolder> {
    private ArrayList<RoomData> mExampleList;
    private HashMap<String, ArrayList<String>> nameIdMap;
    private String user;
    private String multi;
    private String invite = "";
    private Context context;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);


    public static class ExampleViewHolder extends RecyclerView.ViewHolder  {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageButton enterRoom;
        public ImageButton cancelRoom;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            enterRoom = itemView.findViewById(R.id.image_enter_room);
            cancelRoom = itemView.findViewById(R.id.image_cancel_room);

        }
    }

    public RoomAdapterActivity(ArrayList<RoomData> exampleList, String user,
                               HashMap<String, ArrayList<String>> nameIdMap, String multi, Context context ) {
        this.mExampleList = exampleList;
        this.user = user;
        this.nameIdMap = nameIdMap;
        this.multi = multi;
        this.context = context;
    }
    public RoomAdapterActivity(ArrayList<RoomData> exampleList, String user,
                               HashMap<String, ArrayList<String>> nameIdMap, String multi, Context context, String invite) {
        this.mExampleList = exampleList;
        this.user = user;
        this.nameIdMap = nameIdMap;
        this.multi = multi;
        this.context = context;
        this.invite = invite;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_room, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        RoomData currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        if(!this.invite.equals("invited")){
            holder.cancelRoom.setVisibility(View.GONE);
        }

        holder.enterRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.enterRoom.setImageResource(R.drawable.multimedia_green);
                String entryNameRoom = currentItem.getText2();
                ArrayList<String> list = new ArrayList<>();
                list = nameIdMap.get(entryNameRoom);
                String myid = list.get(0);
                String domain = list.get(1);
                if(domain.equals("1")) domain = "GEOGRAPHY";
                if(domain.equals("2")) domain = "HISTORY";
                if(domain.equals("3")) domain = "SCIENCE";
                if(domain.equals("4")) domain = "MOVIES & CELEBRITIES";
                if(domain.equals("5")) domain = "ARTS & SPORTS";

                if (!user.equals(entryNameRoom)) {
                    JSONObject sendJson = new JSONObject();

                    try {
                        sendJson.put("username", user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendJson.put("id", myid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<ResponseBody> mService = service.chooseRoom(sendJson);
                    String finalDomain = domain;
                    mService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String enter = response.body().string();
//                                        Log.v("ATGUL", enter);
                                if (!enter.contains("Done")) {
                                    Intent intent = new Intent(context, GameActivity.class);
//                                            Log.v("TAGUL", myid);
                                    intent.putExtra("USERNAME", user);
                                    intent.putExtra("GAMESID", myid);
                                    intent.putExtra("TOPIC", finalDomain);
                                    intent.putExtra("MULTI", multi);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Room already taken", Toast.LENGTH_SHORT).show();
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


            }
        });

        holder.cancelRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryNameRoom = currentItem.getText2();
                ArrayList<String> list = new ArrayList<>();
                list = nameIdMap.get(entryNameRoom);

                String myid = list.get(0);

                if (!user.equals(entryNameRoom)) {
                    JSONObject sendJson = new JSONObject();
                    try {
                        sendJson.put("id", myid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<ResponseBody> mService = service.decline_invite(sendJson);

                    mService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String enter = response.body().string();
//                                Log.v("ATGUL", enter);
                                if (enter.equals("done")) {
                                    Toast.makeText(context, "Invite declined", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(context, "Invite not found", Toast.LENGTH_LONG).show();
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

            }
        });


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}

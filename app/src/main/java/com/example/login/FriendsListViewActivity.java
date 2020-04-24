package com.example.login;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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

    String userName;

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

        addNewFriendButton = findViewById(R.id.addNewFriendButton);
        addNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_add_new_friend);
                dialog.setTitle("Enter a new friend");

                // set the custom dialog components - text, image and button
                EditText friendNameText = dialog.findViewById(R.id.friendToBeAdded);

                Button dialogOKButton = dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the dialog
                dialogOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

//                Intent intent = new Intent(getApplicationContext(), FriendsListViewActivity.class);
////                intent.putExtra("USERNAME", userName);
//                startActivity(intent);

                dialog.show();
            }
        });


        listView = findViewById(R.id.listView);
        friendsArrayAdapter = new FriendsArrayAdapter(getApplicationContext(), R.layout.activity_listview_row_layout);
        listView.setAdapter(friendsArrayAdapter);

        List<String[]> friendList = readData();
        for(String[] friendData:friendList ) {
            String fruitName = friendData[0];
            String nrFriendPoints = friendData[1];

            FriendData friend = new FriendData(fruitName, nrFriendPoints);
            friendsArrayAdapter.add(friend);
        }
    }

    public List<String[]> readData(){
        List<String[]> resultList = new ArrayList<>();

        String[] fruit7 = new String[2];
        fruit7[0] = "orange";
        fruit7[1] = "47 Calories";
        resultList.add(fruit7);

        String[] fruit1 = new String[2];
        fruit1[0] = "cherry";
        fruit1[1] = "50 Calories";
        resultList.add(fruit1);


        String[] fruit3 = new String[2];
        fruit3[0] = "banana";
        fruit3[1] = "89 Calories";
        resultList.add(fruit3);

        String[] fruit4 = new String[2];
        fruit4[0] = "apple";
        fruit4[1] = "52 Calories";
        resultList.add(fruit4);

        String[] fruit10 = new String[2];
        fruit10[0] = "kiwi";
        fruit10[1] = "61 Calories";
        resultList.add(fruit10);

        String[] fruit5 = new String[2];
        fruit5[0] = "pear";
        fruit5[1] = "57 Calories";
        resultList.add(fruit5);


        String[] fruit2 = new String[2];
        fruit2[0] = "strawberry";
        fruit2[1] = "33 Calories";
        resultList.add(fruit2);

        String[] fruit6 = new String[2];
        fruit6[0] = "lemon";
        fruit6[1] = "29 Calories";
        resultList.add(fruit6);

        String[] fruit8 = new String[2];
        fruit8[0] = "peach";
        fruit8[1] = "39 Calories";
        resultList.add(fruit8);

        String[] fruit9 = new String[2];
        fruit9[0] = "apricot";
        fruit9[1] = "48 Calories";
        resultList.add(fruit9);

        String[] fruit11 = new String[2];
        fruit11[0] = "mango";
        fruit11[1] = "60 Calories";
        resultList.add(fruit11);

        String[] fruit15 = new String[2];
        fruit15[0] = "blueberry";
        fruit15[1] = "60 Calories";
        resultList.add(fruit15);

        return  resultList;
    }

}
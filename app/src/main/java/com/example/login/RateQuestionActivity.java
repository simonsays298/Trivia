package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RateQuestionActivity extends AppCompatActivity {
    private ListView qListView;

    private static int colorIndex;

    final Context context = this;

    private QuestionArrayAdapter questionArrayAdapter;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_question);
        colorIndex = 0;

        qListView = (ListView) findViewById(R.id.qSuggestionListView);

        questionArrayAdapter = new QuestionArrayAdapter(getApplicationContext(),
                R.layout.q_suggestion_listview_row_layout);
        qListView.setAdapter(questionArrayAdapter);

        List<String[]> friendList = readData();
        for (String[] friendData : friendList) {
            String questionName = friendData[0];
            String rightAnswer = friendData[0];
            String wrongAnswer1 = friendData[0];
            String wrongAnswer2 = friendData[0];
            String wrongAnswer3 = friendData[0];
            String domain = friendData[1];

            QuestionData q = new QuestionData(questionName, rightAnswer,
                    wrongAnswer1, wrongAnswer2, wrongAnswer3, domain);
            questionArrayAdapter.add(q);
        }
    }

    public List<String[]> readData() {
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


        return resultList;
    }
}

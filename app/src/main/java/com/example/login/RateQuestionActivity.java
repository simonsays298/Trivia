package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

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

public class RateQuestionActivity extends AppCompatActivity {
    private ListView qListView;

    private static int colorIndex;

    private ImageButton rateButton;

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

        Call<ResponseBody> mService = service.get_suggested_questions();

        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                String suggestedQAndA = null;
                try {
                    suggestedQAndA = response.body().string();
                    Log.v("FRAGEN:", suggestedQAndA);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject listObj = null;
                try {
                    assert suggestedQAndA != null;
                    listObj = new JSONObject(suggestedQAndA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int jsonLength = listObj.length();
                Log.v("SIZE:", String.valueOf(jsonLength));

                for (int i = 0; i < jsonLength; i++) {
                    try {
                        String qAItem = listObj.getString(String.valueOf(i));

                        assert qAItem != null;
                        JSONObject qaObj = new JSONObject(qAItem);

                        String wrongAnswer1 = qaObj.getString("answer0");
                        String wrongAnswer2 = qaObj.getString("answer1");
                        String wrongAnswer3 = qaObj.getString("answer2");
                        String rightAnswer = qaObj.getString("answer3");
                        String domain = null;
                        if (qaObj.getString("domain").equals("1"))
                            domain = "Geography".toUpperCase();
                        else if (qaObj.getString("domain").equals("2"))
                            domain = "History".toUpperCase();
                        else if (qaObj.getString("domain").equals("3"))
                            domain = "Science".toUpperCase();
                        else if (qaObj.getString("domain").equals("4"))
                            domain = "Movies & Celebrities".toUpperCase();
                        else if (qaObj.getString("domain").equals("5"))
                            domain = "Arts & Sports".toUpperCase();


                        String question = qaObj.getString("question");

                        QuestionData suggestedQ = new QuestionData(question, rightAnswer,
                                wrongAnswer1, wrongAnswer2, wrongAnswer3, domain);

                        questionArrayAdapter.insert(suggestedQ, i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("FAIL User Q List View", t.getMessage());

            }
        });

    }
}

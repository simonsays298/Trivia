package com.example.login;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

class QuestionArrayAdapter extends ArrayAdapter<QuestionData> {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    private List<QuestionData> questionDataArrayList = new ArrayList<QuestionData>();

    static class QuestionViewHolder {
        TextView questionNameTextView;
        TextView domainTextView;
        TextView rightAnsTextView;
        ImageButton rateUserQButton;
    }

    public QuestionArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(QuestionData object) {
        questionDataArrayList.add(object);
        super.add(object);
    }

    @Override
    public void insert(QuestionData object, int index) {
        questionDataArrayList.add(index, object);
        super.insert(object, index);
    }

    @Override
    public int getCount() {
        return this.questionDataArrayList.size();
    }

    @Override
    public QuestionData getItem(int index) {
        return this.questionDataArrayList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        QuestionArrayAdapter.QuestionViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.q_suggestion_listview_row_layout, parent, false);
            viewHolder = new QuestionArrayAdapter.QuestionViewHolder();

            viewHolder.questionNameTextView = row.findViewById(R.id.questionName);
            viewHolder.domainTextView = row.findViewById(R.id.domain);
            viewHolder.rightAnsTextView = row.findViewById(R.id.rightAns);
            viewHolder.rateUserQButton = row.findViewById(R.id.rateBlackButton);

            row.setTag(viewHolder);
        } else {
            viewHolder = (QuestionArrayAdapter.QuestionViewHolder) row.getTag();

        }
        QuestionData q = getItem(position);
        viewHolder.questionNameTextView.setText(q.getQuestionName());
        viewHolder.domainTextView.setText(q.getDomain());
        viewHolder.rightAnsTextView.setText(q.getRightAnswer());
        viewHolder.rateUserQButton = row.findViewById(R.id.rateBlackButton);

        viewHolder.rateUserQButton.setOnClickListener(new View.OnClickListener() {
            //When check = 1, you have your FIRST image set to the button
            int check = 1;

            @Override
            public void onClick(View v) {
                // Change image button on click there.
                if (check == 1) {
                    ((ImageButton) v).setImageResource(R.drawable.good1);
                    check = 0;
                    String result = "{\"question\":\"" + q.getQuestionName() + "\"}";
                    Call<ResponseBody> mService = service.rate_question(result);
                    mService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            assert response.body() != null;
                            String atext = null;
                            try {
                                atext = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.v("RATE QUESTION", atext);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                } else {
                    ((ImageButton) v).setImageResource(R.drawable.good);
                    check = 1;
                }
            }
        });

        return row;
    }
}

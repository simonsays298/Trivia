package com.example.login;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class QuestionDataAdapter extends RecyclerView.Adapter<QuestionDataAdapter.QuestionViewHolder> {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    private List<QuestionData> questionDataArrayList;

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionNameTextView;
        TextView domainTextView;
        TextView rightAnsTextView;
        ImageButton rateUserQButton;

        public QuestionViewHolder(View view) {
            super(view);
            this.questionNameTextView = view.findViewById(R.id.questionName);
            this.domainTextView = view.findViewById(R.id.domain);
            this.rightAnsTextView = view.findViewById(R.id.rightAns);
            this.rateUserQButton = view.findViewById(R.id.rateBlackButton);
        }
    }

    public QuestionDataAdapter(List<QuestionData> questionDataArrayList) {
        this.questionDataArrayList = questionDataArrayList;
    }


    @Override
    public QuestionDataAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.q_suggestion_recyclerview_row_layout, parent, false);

        return new QuestionDataAdapter.QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuestionDataAdapter.QuestionViewHolder viewHolder, int position) {

        QuestionData q = questionDataArrayList.get(position);
        viewHolder.questionNameTextView.setText(q.getQuestionName());
        viewHolder.domainTextView.setText(q.getDomain());
        viewHolder.rightAnsTextView.setText(q.getRightAnswer());

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
                            Log.v("RATE", atext);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.v("Fail at Rate", Objects.requireNonNull(t.getMessage()));
                        }
                    });
                } else {
                    ((ImageButton) v).setImageResource(R.drawable.good);
                    check = 1;
                    String result = "{\"question\":\"" + q.getQuestionName() + "\"}";
                    Call<ResponseBody> mService = service.unrate_question(result);
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
                            Log.v("UNRATE", atext);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.v("Fail at Unrate", Objects.requireNonNull(t.getMessage()));
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionDataArrayList.size();
    }

}

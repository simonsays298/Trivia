package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class QuestionArrayAdapter extends ArrayAdapter<QuestionData> {

    private List<QuestionData> questionDataArrayList = new ArrayList<QuestionData>();

    static class QuestionViewHolder {
        TextView questionNameTextView;
        TextView domainTextView;
        TextView rightAnsTextView;
        TextView wrongAns1TextView;
        TextView wrongAns2TextView;
        TextView wrongAns3TextView;

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
            viewHolder.wrongAns1TextView = row.findViewById(R.id.wrongAns1);
            viewHolder.wrongAns2TextView = row.findViewById(R.id.wrongAns2);
            viewHolder.wrongAns3TextView = row.findViewById(R.id.wrongAns3);

            row.setTag(viewHolder);
        } else {
            viewHolder = (QuestionArrayAdapter.QuestionViewHolder)row.getTag();
        }
        QuestionData q = getItem(position);
        viewHolder.questionNameTextView.setText(q.getQuestionName());
        viewHolder.domainTextView.setText(q.getDomain());
        viewHolder.rightAnsTextView.setText(q.getRightAnswer());
        viewHolder.wrongAns1TextView.setText(q.getWrongAnswer1());
        viewHolder.wrongAns2TextView.setText(q.getWrongAnswer2());
        viewHolder.wrongAns3TextView.setText(q.getWrongAnswer3());

        return row;
    }
}

package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendDataAdapter extends RecyclerView.Adapter<FriendDataAdapter.FriendViewHolder> {
    private static final String TAG = "FriendDataAdapter";
     List<FriendData> friendDataArrayList;

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendNameTextView;
        TextView nrPointsFriendTextView;

        public FriendViewHolder(View view) {
            super(view);
            this.friendNameTextView = view.findViewById(R.id.friendName);
            this.nrPointsFriendTextView = view.findViewById(R.id.pointsFriend);
        }
    }

    public FriendDataAdapter(List<FriendData> friendDataArrayList) {
        this.friendDataArrayList = friendDataArrayList;
    }

    public void add(FriendData friendData){
        this.friendDataArrayList.add(friendData);
    }


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendlist_recyclerview_row_layout, parent, false);

        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder viewHolder, int position) {

        FriendData friend = friendDataArrayList.get(position);
        viewHolder.friendNameTextView.setText(friend.getFriendName());
        viewHolder.nrPointsFriendTextView.setText(friend.getNrFriendPoints());

    }

    @Override
    public int getItemCount() {
        return friendDataArrayList.size();
    }

}
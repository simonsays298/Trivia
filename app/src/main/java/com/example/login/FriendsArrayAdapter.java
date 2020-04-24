package com.example.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FriendsArrayAdapter extends ArrayAdapter<FriendData> {
    private static final String TAG = "FriendArrayAdapter";
    private List<FriendData> friendDataList = new ArrayList<FriendData>();

    static class FriendViewHolder {
        TextView friendNameTextView;
        TextView nrPointsFriendTextView;
    }

    public FriendsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(FriendData object) {
        friendDataList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.friendDataList.size();
    }

    @Override
    public FriendData getItem(int index) {
        return this.friendDataList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriendViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_listview_row_layout, parent, false);
            viewHolder = new FriendViewHolder();
            viewHolder.friendNameTextView = row.findViewById(R.id.friendName);
            viewHolder.nrPointsFriendTextView = row.findViewById(R.id.pointsFriend);
            row.setTag(viewHolder);
        } else {
            viewHolder = (FriendViewHolder)row.getTag();
        }
        FriendData friend = getItem(position);
        viewHolder.friendNameTextView.setText(friend.getFriendName());
        viewHolder.nrPointsFriendTextView.setText(friend.getNrFriendPoints());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
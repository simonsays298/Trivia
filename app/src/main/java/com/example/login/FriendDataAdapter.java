package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendDataAdapter extends RecyclerView.Adapter<FriendDataAdapter.FriendViewHolder> {
    private static final String TAG = "FriendArrayAdapter";
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


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listview_row_layout, parent, false);

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

//    public FriendsArrayAdapter(Context context, int textViewResourceId) {
//        super(context, textViewResourceId);
//    }
//
//    @Override
//    public void add(FriendData object) {
//        friendDataArrayList.add(object);
//        super.add(object);
//    }
//
//    @Override
//    public void insert(FriendData object, int index) {
//        friendDataArrayList.add(index, object);
//        super.insert(object, index);
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        FriendViewHolder viewHolder;
//        if (row == null) {
//            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(R.layout.activity_listview_row_layout, parent, false);
//            viewHolder = new FriendViewHolder();
//            viewHolder.friendNameTextView = row.findViewById(R.id.friendName);
//            viewHolder.nrPointsFriendTextView = row.findViewById(R.id.pointsFriend);
//            row.setTag(viewHolder);
//        } else {
//            viewHolder = (FriendViewHolder)row.getTag();
//        }
//        FriendData friend = getItem(position);
//        viewHolder.friendNameTextView.setText(friend.getFriendName());
//        viewHolder.nrPointsFriendTextView.setText(friend.getNrFriendPoints());
//        return row;
//    }
//
//    public Bitmap decodeToBitmap(byte[] decodedByte) {
//        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//    }
}
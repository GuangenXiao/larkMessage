package com.example.larkmessage.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.R;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;

import java.util.ArrayList;
import java.util.List;

import io.grpc.internal.SharedResourceHolder;
import io.opencensus.resource.Resource;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
private static List<Friend> list = new ArrayList<Friend>();
private Context context;
private RecyclerView mRecyclerView;

    public FriendAdapter(Context context) {
        this.context =context;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_view,parent,false);

        return new FriendAdapter.FriendViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView =recyclerView;
    }
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.icon.setImageResource(list.get(position).getIcon());
        holder.username.setText(list.get(position).getUserName());
        holder.email.setText(list.get(position).getEmail());
    }
    public void addAll(List<Friend> ItemList)
    {
        list =  ItemList;
        notifyDataSetChanged();
        if(list.size()>0)mRecyclerView.getLayoutManager().scrollToPosition( list.size() );
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder
    {
        private TextView username;
        private ImageView icon;
        private  TextView email;
        private CardView cardView;
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.Friend_CardView);
            icon =itemView.findViewById(R.id.friend_icon);
            email=itemView.findViewById(R.id.friend_email);
            username =itemView.findViewById(R.id.friend_name);
        }
    }
}

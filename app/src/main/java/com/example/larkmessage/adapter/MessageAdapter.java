package com.example.larkmessage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.R;
import com.example.larkmessage.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>  {
    private static List<Message> list;
    private RecyclerView mRecycleView;
    private Context context;
    MessageAdapter(Context context)
    {
        this.context =context;
        list = new ArrayList<Message>();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycleView =recyclerView;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view,parent,false);

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
       /* holder.context.setText(list.get(position).getConetxt());
        if(list.get(position).getTime().length()>20)holder.time.setText(list.get(position).getTime().substring(0, 20));
        else holder.time.setText(list.get(position).getTime());
        holder.username.setText(list.get(position).getUsername());*/

    }

    public void addAll(List<Message> massageItemList)
    {
        list = massageItemList;
        notifyDataSetChanged();
        if(list.size()>0)mRecycleView.getLayoutManager().scrollToPosition( list.size() );
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private  TextView context;
        private  TextView time;
        private CardView cardView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.Message_CardView);
            /*username = itemView.findViewById(R.id.massage_user);
            context =itemView.findViewById(R.id.massage_context);
            time  =itemView.findViewById(R.id.massage_date);*/
        }

    }




}
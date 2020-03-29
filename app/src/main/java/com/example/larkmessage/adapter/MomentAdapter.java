package com.example.larkmessage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.R;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Moment;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.fileUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentHolder>{

    private static List<Moment> list = new ArrayList<Moment>();
    private Context context;
    private RecyclerView mRecyclerView;
    private UserItem userItem;

    public MomentAdapter(Context context, UserItem userItem)
    {
        this.context =context;
        this.userItem =userItem;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView =recyclerView;
    }
    @NonNull
    @Override
    public MomentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_view,parent,false);
        return new MomentAdapter.MomentHolder(itemView);
    }
    public void  addAll(ArrayList<Moment> itemList)
    {
        list =  itemList;
        notifyDataSetChanged();
        if(list.size()>0)mRecyclerView.getLayoutManager().scrollToPosition( list.size() );
    }
    @Override
    public void onBindViewHolder(@NonNull MomentHolder holder, int position) {
        holder.userName .setText(list.get(position).getUserName());
        holder.context.setText(list.get(position).getText());
        holder.icon.setImageResource(list.get(position).getIcon());
       // Bitmap bm = BitmapFactory.decodeFile(list.get(position).getImage());
        //holder.image.setImageBitmap(bm);
        try {
            new fileUnit().downloadImage(list.get(position),holder.image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MomentHolder extends RecyclerView.ViewHolder
    {
        private CardView card;
        private ImageView icon;
        private ImageView image;
        private ImageButton agree;
        private ImageButton disagree;
        private TextView context;
        private TextView userName;

        public MomentHolder(@NonNull View itemView) {
            super(itemView);

            card =itemView.findViewById(R.id.moment_card);
            icon = itemView.findViewById(R.id.moment_icon);
            image = itemView.findViewById(R.id.moment_image);
            agree = itemView.findViewById(R.id.moment_agree);
            disagree =itemView.findViewById(R.id.moment_disagree);
            context =itemView.findViewById(R.id.moment_context);
            userName =itemView.findViewById(R.id.moment_name);
        }
    }
}

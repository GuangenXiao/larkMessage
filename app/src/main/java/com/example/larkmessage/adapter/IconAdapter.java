package com.example.larkmessage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.R;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.IconDB;

import java.util.ArrayList;

public class IconAdapter extends  RecyclerView.Adapter<IconAdapter.IconViewHolder>{
    private ArrayList<Integer> list = null;
    private UserItem userItem=null;
    private Context context=null;
    private IconDB iconDB = new IconDB();
    private Integer icon=null;
    private CardView currentCard=null;
    public IconAdapter(UserItem userItem, Context context)
    {
        list =new ArrayList<Integer>();
        this.userItem =userItem;
        this.context =context;
    }
    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_view,parent,false);
        return new IconAdapter.IconViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final IconViewHolder holder, final int position) {
        holder.image.setImageResource(iconDB.getIconID(list.get(position)));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentCard!=null) currentCard.setBackgroundColor(v.getContext().getResources().getColor(R.color.white));
                icon = list.get(position);
                currentCard = holder.card;
                if(currentCard!=null)currentCard.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    public  int getChoosedIcon()
    {
        return icon;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void  addAll( ArrayList<Integer> arrayList)
    {
        list = arrayList;
        notifyDataSetChanged();
    }
    class IconViewHolder extends RecyclerView.ViewHolder
    {
        private CardView card;
        private ImageView image;
        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            card =itemView.findViewById(R.id.itemIcon_cardView);
            image = itemView.findViewById(R.id.itemIcon_imageView);
        }
    }
}

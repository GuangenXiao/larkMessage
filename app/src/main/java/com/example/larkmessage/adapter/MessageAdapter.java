package com.example.larkmessage.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.R;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.Moment;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.fileUnit;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>  {
    private static List<Message> list;
    private RecyclerView mRecycleView;
    private Context context;
    private Friend friend;
    private  UserItem userItem;

    private int SENDER_TYPE = 1;
    private int RECEIVER_TYPE = 2;
    public MessageAdapter(Context context)
    {
        this.context =context;
        list = new ArrayList<Message>();
    }
    public void  setUserAndFriend(Friend friend, UserItem userItem)
    {
        this.userItem =userItem;
        this.friend =friend;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycleView =recyclerView;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View viewOne = layoutInflater.inflate(R.layout.message_view, parent, false);
                return new MessageViewHolder(viewOne);

    }
    protected  void deleteMessage(Message message)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Chatting").document(friend.getMessageId()).collection("MessageList").document(message.getPath())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    protected void  showDeleteDialog(final Message message)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.delete_this_moment);
        alterDiaglog.setMessage(R.string.make_sure_delete_message);
        alterDiaglog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton(R.string.Remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMessage(message);
            }
        });
        alterDiaglog.show();
    }
    protected void  showIgnoreDialog(final Message message)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.delete_this_message);
        alterDiaglog.setMessage(R.string.only_sender_can_delete_message);
        alterDiaglog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.show();
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
       holder.context.setText(list.get(position).getContext());
        if(list.get(position).getTime().length()>20)holder.time.setText(list.get(position).getTime().substring(0, 20));
        else holder.time.setText(list.get(position).getTime());
        holder.username.setText(list.get(position).getUsername());
        if(list.get(position).getImageResource()!=null) {
            try {
                new fileUnit().downloadMessageImage(list.get(position),holder.image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            holder.image.setImageDrawable(null);
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(list.get(position).getUsername().equals(userItem.getUserName()))
                {
                    showDeleteDialog(list.get(position));
                }
                else
                {
                    showIgnoreDialog(list.get(position));
                }


                return false;
            }
        });
    }

    public void addAll(List<Message> massageItemList)
    {
        list=null;
        list = massageItemList;
        notifyDataSetChanged();
    }
    public void addItem()
    {
        //mNames.add(0,getRandomName());
        //notifyDataSetChanged();
        notifyItemInserted(0);
        // notifyItemRangeChanged(0,mNames.size());
        //mRecycleView.getLayoutManager().scrollToPosition(0);

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
        private ImageView image;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.Message_CardView);
            username = itemView.findViewById(R.id.message_user);
            context =itemView.findViewById(R.id.message_context);
            time  =itemView.findViewById(R.id.message_date);
            image = itemView.findViewById(R.id.message_image);
        }

    }




}
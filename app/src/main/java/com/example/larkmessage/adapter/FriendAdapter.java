package com.example.larkmessage.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.MessageActivity;
import com.example.larkmessage.R;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.UserItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
private static List<Friend> list = new ArrayList<Friend>();
private Context context;
private RecyclerView mRecyclerView;
private UserItem userItem;

    public FriendAdapter(Context context ,UserItem userItem) {
        this.context =context;
        this.userItem=userItem;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_view,parent,false);

        return new FriendViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView =recyclerView;
    }
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, final int position) {
        if(list.get(position).getIcon()!=null)
        {
            try {
                holder.icon.setImageResource(list.get(position).getIcon());
            }
        catch (Exception e)
        {

        }}
        holder.username.setText(list.get(position).getUserName());
        if(list.get(position).getType()==true)holder.email.setText(list.get(position).getEmail());
        else holder.email.setText("waiting for your response");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(position);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showRefuseDialog(position);
                return false;
            }
        });
    }
    protected void  showRefuseDialog(final Integer position)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Are you sure to delete your friends?");
        alterDiaglog.setMessage("this option can't be cancelled.");
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refuseFriends(list.get(position ));
            }
        });
        alterDiaglog.show();
    }
    protected void checkStatus(final Integer position)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(list.get(position).getEmail()).collection("FriendList").document(userItem.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       Friend friend= documentSnapshot.toObject(Friend.class);
                       try {
                           if (friend.getType() == false) {
                               showWaitingDialog();
                           } else {
                               branchToMessageAt(list.get(position));
                           }
                       }
                       catch (Exception e)
                       {

                       }
                    }
                });
    }
    protected void branchToMessageAt(Friend friend)
    {
        Intent intent = new Intent( context,MessageActivity.class);
        Bundle bundle =new Bundle();
        intent.putExtra("friend",friend);
        intent.putExtra("user",userItem);
        startActivity(context,intent,bundle);
    }
    protected void  showWaitingDialog()
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.friend_dialog);
        alterDiaglog.setMessage("Waiting for friend's responds");
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.show();
    }
    protected void refuseFriends(final Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(userItem.getEmail()).collection("FriendList").document(friend.getEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteYourself(friend);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    protected void deleteYourself(Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(friend.getEmail()).collection("FriendList").document(userItem.getEmail())
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

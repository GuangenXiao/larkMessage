package com.example.larkmessage.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.MainActivity;
import com.example.larkmessage.MessageActivity;
import com.example.larkmessage.NotificationService;
import com.example.larkmessage.R;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.IconDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
    private static int pushNum = 0;


private static List<Friend> list = new ArrayList<Friend>();
private Context context;
private RecyclerView mRecyclerView;
private UserItem userItem;
private IconDB iconDB =new IconDB();
private ArrayList<String> onlineList = new ArrayList<>();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FriendViewHolder holder, final int position) {
        if(iconDB.containKey(list.get(position).getIcon()))
        {
            holder.icon.setImageResource(iconDB.getIconID(list.get(position).getIcon()));
        }
        else if(iconDB.containValue(list.get(position).getIcon()))
        {
            holder.icon.setImageResource(list.get(position).getIcon());
        }
        else
        {
            holder.icon.setImageResource(iconDB.getIconID(iconDB.getDefaultIcon()));
        }

        holder.username.setText(list.get(position).getUserName());


        holder.username.setText(list.get(position).getUserName());
        try {
            if(list.get(position).getNickName()!=null&&list.get(position).getNickName().length()>=0)holder.username.setText(list.get(position).getNickName());
        }
        catch (Exception e1)
        {

        }
        if(list.get(position).getType()==true)holder.email.setText(list.get(position).getEmail());
        else {
            holder.email.setText(R.string.waiting_for_your_resopone);
            if(list.get(position).getStatus()==false)
            {
               /* Intent intent= new Intent(context, NotificationService.class);
                Bundle bundle = new Bundle();
                intent.putExtra("friend",list.get(position));
                intent.putExtra("user",userItem);
                intent.putExtra("int",position);
                context.startService(intent);*/
                showChannel1Notification(context,list.get(position),userItem,position);
                updateStatus(list.get(position),userItem);
            }
        }

    }
    protected void  showRefuseDialog(final Integer position)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.question_delete_friend);
        alterDiaglog.setMessage(R.string.opotion_cant_cancel_warning);
        alterDiaglog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
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
        alterDiaglog.setMessage(R.string.waiting_for_friend_respone);
        alterDiaglog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
    public static void showChannel1Notification(Context context, Friend friend, UserItem userItem, int i) {
  /*      int notificationId = 0x1234;
        Notification.Builder builder = new Notification.Builder(context,"1"); //与channelId对应
        //icon title text必须包含，不然影响桌面图标小红点的展示
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("xxx")
                .setContentText("xxx")
                .setNumber(8); //久按桌面图标时允许的此条通知的数量
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build()); */
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent notificationIntent = new Intent(context, MessageActivity.class);
        notificationIntent.putExtra("friend",friend);
        notificationIntent.putExtra("user",userItem);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        builder.setContentTitle(friend.getUserName()+" want to add you")//设置通知栏标题
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                //.setContentText(anItem.getNote())
                .setNumber(++pushNum)
                .setTicker("add friends notification from "+friend.getUserName()) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.favicon)//设置通知小ICON
                .setChannelId(PUSH_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(i, notification);
        }
    }
    protected void updateStatus(Friend friend,UserItem userItem)
    {
        friend.setStatus(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(userItem.getEmail()).collection("FriendList").document(friend.getEmail())
                .update("status",friend.getStatus())
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

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //holder.cardView.setEnabled(false);
                    checkStatus(getAdapterPosition());
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showRefuseDialog(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}

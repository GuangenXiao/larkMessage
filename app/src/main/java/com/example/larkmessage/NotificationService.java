package com.example.larkmessage;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.UserItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
      */
    public static final String TAG = "NotificationService";
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
    private static int pushNum = 0;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            UserItem userItem = (UserItem) intent.getSerializableExtra("user");
            Friend friend = (Friend) intent.getSerializableExtra("friend");
            //Context context = (Context) intent.getSerializableExtra("context");
            Context context =getApplicationContext();
            Integer i = (Integer) intent.getSerializableExtra("int");
            showChannel1Notification(context,friend,userItem,i);
            updateStatus(friend,userItem);
        }
        catch (Exception e)
        {

        }
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
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
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
}

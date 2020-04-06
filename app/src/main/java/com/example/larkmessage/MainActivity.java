package com.example.larkmessage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.loginUnit;
import com.example.larkmessage.unit.userUnit;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView navMailTextView;
    private TextView navNameTextView;
    private ImageView navIconImageView;
    public static final String KEY_BACKGROUND_COLOR ="KEY_BACKGROUND_COLOR";
    private final static String PREFS = "PREFS";
    private static  final  String KEY_PASSWORD="key_password";
    private  static  final  String KEY_EMAIL ="key_email";
    private UserItem userItem= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            Intent intent = new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        userItem= (UserItem) getIntent().getSerializableExtra("userInfo");
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.hide();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        mAuth = FirebaseAuth.getInstance();

        View v= navigationView.getHeaderView(0);
        navIconImageView = v.findViewById(R.id.navIcon_imageView);
        navMailTextView = v.findViewById(R.id.navMail_textView);
        navNameTextView =v.findViewById(R.id.navName_textView);
        if(user!=null)
        {

            navNameTextView.setText(userItem.getUserName());
            navMailTextView.setText(user.getEmail());
            navIconImageView.setImageResource(userItem.getIcon());
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent( Settings.ACTION_LOCALE_SETTINGS));
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public UserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(UserItem userItem) {
        this.userItem = userItem;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences( PREFS, MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        // Put the other fields into the editor
        editor.commit();
        /*try {
            checkPlan();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    //waiting list
/*
    public static void showChannel1Notification(Context context, PlanningItem anItem, int i) {
       int notificationId = 0x1234;
        Notification.Builder builder = new Notification.Builder(context,"1"); //与channelId对应
        //icon title text必须包含，不然影响桌面图标小红点的展示
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("xxx")
                .setContentText("xxx")
                .setNumber(8); //久按桌面图标时允许的此条通知的数量
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent notificationIntent = new Intent(context, PlanningActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        builder.setContentTitle(anItem.getTitle())//设置通知栏标题
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                //.setContentText(anItem.getNote())
                .setNumber(++pushNum)
                .setTicker(anItem.getNote()) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                .setChannelId(PUSH_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(i, notification);
        }
    }*/
}

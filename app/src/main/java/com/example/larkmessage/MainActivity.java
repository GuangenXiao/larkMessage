package com.example.larkmessage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
            navIconImageView.setImageResource(R.drawable.nn1);
        }


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
}

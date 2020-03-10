package com.example.larkmessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final  int RC_SIGN_IN =123;
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView navMailTextView;
    private TextView navNameTextView;
    private ImageView navIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                loginFcn();
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

            loginFcn();



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


    public void loginFcn()
    {
        List<AuthUI.IdpConfig> list= Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(list).build(),RC_SIGN_IN
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navIconImageView = findViewById(R.id.navIcon_imageView);
        navMailTextView = findViewById(R.id.navMail_textView);
        navNameTextView = findViewById(R.id.navName_textView);
        if(requestCode==RC_SIGN_IN)
        {
            IdpResponse idpResponse =IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("successful"+user.getDisplayName()+user.getEmail()+user.getPhoneNumber());
                navNameTextView.setText(user.getDisplayName());
                navMailTextView.setText(user.getEmail());
                navIconImageView.setImageResource(R.drawable.nn1);
                //Intent intent = new Intent(MainActivity.this,readingActicity.class);
                //intent.putExtra(USERCODE,auth);
                //startActivity(intent);
            }
            else
            {
                if(idpResponse ==null)
                {
                    System.out.println("Sign in cancelled");
                    loginFcn();
                    return;
                }
                if(idpResponse.getError().getErrorCode()== ErrorCodes.NO_NETWORK)
                {
                    System.out.println("no internet connection");
                    loginFcn();
                    return;
                }
            }
        }
    }
}

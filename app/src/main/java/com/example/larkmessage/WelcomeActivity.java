package com.example.larkmessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.larkmessage.unit.loginUnit;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;

public class WelcomeActivity extends AppCompatActivity {

    private EditText welEMailEditView;
    private EditText welPasswordEditView;
    private Button welRegisterButton;
    private Button welSignInButton;
    private String email;
    private String password;
    private Boolean mem=true;
    private CheckBox memCheck;
    private final static String PREFS = "PREFS";
    private static  final  String KEY_PASSWORD="key_password";
    private static  final  String KEY_MEMORY="key_memory";
    private  static  final  String KEY_EMAIL ="key_email";
    private SharedPreferences  preferences;
    private loginUnit loginunit =new loginUnit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
        welEMailEditView =findViewById(R.id.welcome_email_editText);
        welPasswordEditView =findViewById(R.id.welcome_password_editText);
        welRegisterButton =findViewById(R.id.welcome_register_button);
        welSignInButton = findViewById(R.id.welcome_login_button);
        memCheck =findViewById(R.id.mem_CheckBox);
        if(getSharedPreferences(PREFS, MODE_PRIVATE)!=null) {

            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            String email = prefs.getString(KEY_EMAIL,null);
            String password = prefs.getString(KEY_PASSWORD,null);
            mem =prefs.getBoolean(KEY_MEMORY,true);
            if(email!=null)welEMailEditView.setText(email);
            if(password!=null)welPasswordEditView.setText(password);
        }
        email=welEMailEditView.getText().toString();
        password=welPasswordEditView.getText().toString();

        welSignInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email=welEMailEditView.getText().toString();
                        password=welPasswordEditView.getText().toString();
                       loginunit.signInWithEmailAndPassword(email,password,WelcomeActivity.this);

                    }
                }
        );
        welRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
        memCheck.setChecked(mem);

        memCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    mem=true;
                }
                else
                {
                    mem=false;
                }
            }
        });
      /*  if(email.length()>0&&password.length()>0)
        {
            loginunit.signInWithEmailAndPassword(email,password,WelcomeActivity.this);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mem==true) {
            email = welEMailEditView.getText().toString();
            password = welPasswordEditView.getText().toString();
        }
        else
        {
            email="";
            password="";
        }
        preferences = getSharedPreferences(PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_MEMORY,mem);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }
}

package com.example.larkmessage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.loginUnit;

public class RegisterActivity extends AppCompatActivity {
    private Switch passwordSwitch;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;
    private Button cancelButton;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        passwordSwitch =findViewById(R.id.register_password_switch);
        nameEditText = findViewById(R.id.uName_editText);
        emailEditText =findViewById(R.id.uEmail_editText);
        passwordEditText = findViewById(R.id.uPassword_editText);
        phoneEditText =findViewById(R.id.uPhone_editText);
        cancelButton= findViewById(R.id.register_cancel_button);
        registerButton = findViewById(R.id.register_button);
        passwordSwitch.setChecked(true);
        passwordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {

                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                else if(isChecked==false)
                {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =getIntent();
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password =passwordEditText.getText().toString();
                String phone=phoneEditText.getText().toString();
               if(name.length()>0&&email.length()>0&&password.length()>0&&phone.length()>0)
                   new loginUnit().createAccount(new UserItem.Builder(name,email).password(password).phoneNumber(phone).Build(),RegisterActivity.this);

               else
               {
                   Toast.makeText(RegisterActivity.this, "need more info", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }

}

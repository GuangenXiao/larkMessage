package com.example.larkmessage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.larkmessage.adapter.IconAdapter;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.IconDB;
import com.example.larkmessage.unit.ValidationUnit;
import com.example.larkmessage.unit.loginUnit;

import java.text.ParseException;

public class RegisterActivity extends AppCompatActivity {
    private Switch passwordSwitch;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;
    private Button cancelButton;
    private Button registerButton;
    private ImageView iconImage;
    private IconAdapter Adapter;
    private RecyclerView recyclerView;
    private  Integer icon;
    private  IconDB iconDB = new IconDB();
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
        iconImage = findViewById(R.id.uIcon_Image);
        icon = iconDB.getDefaultKey();
        iconImage.setImageResource(iconDB.getIconID(icon));
        iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconDialog();
            }
        });
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

                if(ValidationUnit.checkEmail(email)==false)
                {
                    Toast.makeText(RegisterActivity.this, "Invalued email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ValidationUnit.isMobileNO(phone)==false)
                {
                    Toast.makeText(RegisterActivity.this, "Invalued email phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
               if(name.length()>0&&email.length()>0&&password.length()>0&&phone.length()>0) {
                   try {
                       new loginUnit().createAccount(new UserItem.Builder(name,email).icon(icon).password(password).phoneNumber(phone).Build(),RegisterActivity.this);
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
               }

               else
               {
                   Toast.makeText(RegisterActivity.this, "need more info", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }

    protected void  showIconDialog()
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Choose Your Icon");
        //recyclerView = getView().findViewById(R.id.icon_recycler);
        recyclerView = new RecyclerView(this);
        if(recyclerView==null)return;
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setHasFixedSize(true);
        Adapter =new IconAdapter(new UserItem(),this);
        Adapter.onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(Adapter);
        Adapter.addAll(new IconDB().getList());
        alterDiaglog.setView(recyclerView);
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alterDiaglog.setPositiveButton("accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               icon= Adapter.getChoosedIcon();
               iconImage.setImageResource(iconDB.getIconID(icon));
            }
        });
        alterDiaglog.show();

    }
    protected void setIcon(Integer icon)
    {
        this.icon =icon;
        iconImage.setImageResource(iconDB.getIconID(icon));
    }
}

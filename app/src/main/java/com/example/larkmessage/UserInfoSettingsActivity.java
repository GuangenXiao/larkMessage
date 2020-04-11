package com.example.larkmessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.IconDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoSettingsActivity extends AppCompatActivity {

    private UserItem userItem;
    private Friend friend;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView iconImage;
    private TextView dateTextView;
    private Button acceptButton;
    private IconDB iconDB =new IconDB();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Intent intent= getIntent();
        userItem = (UserItem) intent.getSerializableExtra("user");
        friend = (Friend) intent.getSerializableExtra("friend");

        iconImage =findViewById(R.id.friendSetting_Icon);
        if(iconDB.containKey(userItem.getIcon()))
        {
            iconImage.setImageResource(iconDB.getIconID(userItem.getIcon()));
        }
        else if(iconDB.containValue(userItem.getIcon()))
        {
            iconImage.setImageResource(userItem.getIcon());
        }
        else
        {
            iconImage.setImageResource(iconDB.getDefaultIcon());
        }
        emailTextView =findViewById(R.id.friendSetting_Email);
        emailTextView.setText(friend.getEmail());
        nameTextView = findViewById(R.id.friendSetting_Name);
        nameTextView.setText(friend.getUserName());
        dateTextView = findViewById(R.id.friendSetting_Date);
        dateTextView.setText(friend.getRelationShipDate());
        acceptButton=findViewById(R.id.friendSetting_Button);
        final SettingsFragment settingsFragment= new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings,settingsFragment )
                .commit();
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend =settingsFragment.getFriend();
                updateFriends(friend);

            }
        });
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    protected void updateFriends(final Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(userItem.getEmail()).collection("FriendList").document(friend.getEmail())
                .set(friend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        private EditTextPreference nickNameEdit;
        private SwitchPreferenceCompat friendSwitch;
        private  SwitchPreferenceCompat momentSwitch;
        private Friend friend;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            friend =((UserInfoSettingsActivity)getActivity()).getFriend();
            nickNameEdit = findPreference("signature");
            friendSwitch =findPreference("friend");
            momentSwitch =findPreference("moment");
            nickNameEdit.setText(friend.getNickName());
            if(friend.getType()!=null)friendSwitch.setChecked(friend.getType());
            if(friend.getLetHimAccessMoment()!=null)momentSwitch.setChecked(friend.getLetHimAccessMoment());
            friendSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    friend.setType((Boolean) newValue);
                    return true;
                }
            });
            momentSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    friend.setLetHimAccessMoment((Boolean) newValue);
                    return true;
                }
            });


        }

        public Friend getFriend() {
            friend.setNickName(nickNameEdit.getText());
            return friend;
        }

    }
}
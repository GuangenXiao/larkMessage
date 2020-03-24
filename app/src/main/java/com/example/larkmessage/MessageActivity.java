package com.example.larkmessage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.larkmessage.adapter.FriendAdapter;
import com.example.larkmessage.adapter.MessageAdapter;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.UserItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private UserItem userItem;
    private Friend friend;
    private MessageAdapter Adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent =getIntent();
        userItem = (UserItem) intent.getSerializableExtra("user");
        friend = (Friend) intent.getSerializableExtra("friend");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setTitle(friend.getUserName());
        }
        recyclerView = findViewById(R.id.message_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Adapter =new MessageAdapter(this);
        recyclerView.setAdapter(Adapter);
    }

    protected void writeData(Message message)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> newUser = new HashMap<>();
        db.collection("UserList").document(userItem.getEmail()).collection("").document(message.getReceiver())
                .set(newUser)
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

    protected  void getData()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Message")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                    {
                                        Map<String,Object> m = documentSnapshot.getData();
                                    }
                                }
                                else
                                {
                                    Log.w("tag", "error getting document ", task.getException());
                                }
                            }
                        }
                );

        // Log.w("listsize", "error getting document "+list.size());

        //adapter.notifyDataSetChanged();
        //fragment.getView().requestLayout();
        // adapter = new MassageAdapter(this,list);
        //recyclerView.setAdapter(adapter);
    }
}

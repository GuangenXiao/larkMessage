package com.example.larkmessage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devs.squaremenu.OnMenuClickListener;
import com.devs.squaremenu.SquareMenu;
import com.example.larkmessage.adapter.FriendAdapter;
import com.example.larkmessage.adapter.MessageAdapter;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.DateUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private UserItem userItem;
    private Friend friend;
    private MessageAdapter Adapter;
    private RecyclerView recyclerView;
    private Button button;
    private EditText editText;
    private ArrayList<Message> list;
    final Comparator c =new Comparator<Message>() {
        @Override
        public int compare(Message o1, Message o2) {
            // if(MassageItem)
            try {
                if (DateUnit.convertStringToDateAddTime(o1.getTime()).before(DateUnit.convertStringToDateAddTime(o2.getTime()))) {
                    return -1;
                } else return 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 1;
        }
    };
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
        Adapter.setUserAndFriend(friend,userItem);
        Adapter.onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(Adapter);
        button = findViewById(R.id.message_button);
        editText = findViewById(R.id.message_editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message  m= new Message();
                m.setUsername(userItem.getUserName());
                m.setReceiver(friend.getEmail());
                m.setContext(editText.getText().toString());
                editText.setText(null);
                try {
                    SendMessage(m);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(Adapter.getItemCount()-1);

            }
        });


                recyclerView.scrollToPosition(Adapter.getItemCount()-1);

        setListener(friend);
        floatTool();

    }
     void  floatTool()
    {
        SquareMenu mSquareMenu = (SquareMenu) findViewById(R.id.square_menu);
        mSquareMenu.setOnMenuClickListener(new OnMenuClickListener(){
            @Override
            public void onMenuOpen() {  }
            @Override
            public void onMenuClose() { }
            @Override
            public void onClickMenu1() {
                Intent intent =new Intent(MessageActivity.this,MapsActivity.class);
                startActivity(intent);
            }
            @Override
            public void onClickMenu2() { }
            @Override
            public void onClickMenu3() { }
        });
    }

    protected void SendMessage(Message message) throws ParseException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Chatting").document(friend.getMessageId()).collection("MessageList").document(userItem.getUserName()+ DateUnit.getSystemTimeAndDate())
                .set(message)
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

    protected  void setListener(final Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference ref =db.collection("Chatting").document(friend.getMessageId()).collection("MessageList");
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Message error", "Listen failed.", e);
                    return;
                }
                getMessage(friend);
            }
        });
    }
    protected  void getMessage(Friend friend)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        db.collection("Chatting").document(friend.getMessageId()).collection("MessageList")
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
                                       list.add(documentSnapshot.toObject(Message.class));

                                    }
                                    list.sort(c);
                                    Adapter.addAll(list);
                                    recyclerView.scrollToPosition(Adapter.getItemCount()-1);
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

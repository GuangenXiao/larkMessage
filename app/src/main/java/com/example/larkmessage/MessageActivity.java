package com.example.larkmessage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devs.squaremenu.OnMenuClickListener;
import com.devs.squaremenu.SquareMenu;
import com.example.larkmessage.adapter.MessageAdapter;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.DateUnit;
import com.example.larkmessage.unit.fileUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;

public class MessageActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_GALLERY = 0x10;// 图库选取图片标识请求码
    private static final int CROP_PHOTO = 0x12;// 裁剪图片标识请求码
    private static final int STORAGE_PERMISSION = 0x20;// 动态申请存储权限标识


    private Uri uri = null;// 裁剪后的图片uri
    private String path = "";

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
        if(userItem.getBgColor()!=null) {
            recyclerView.setBackgroundColor(userItem.getBgColor());
            findViewById(R.id.Message_coorLay).setBackgroundColor(userItem.getBgColor());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message  m= new Message();
                m.setUsername(userItem.getUserName());
                m.setReceiver(friend.getEmail());
                m.setContext(editText.getText().toString());
                editText.setText(null);
                try {
                    if(m.getContext().length()>0)
                    SendMessage(m);
                    else {Toast.makeText(MessageActivity.this, "empty message", Toast.LENGTH_SHORT).show();}
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
        if(friend.getType()==false)showAcceptDialog();
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
                intent.putExtra("friend",friend);
                intent.putExtra("user",userItem);
                startActivity(intent);
            }
            @Override
            public void onClickMenu2() {
                gallery();
            }
            @Override
            public void onClickMenu3() {
                Intent intent =new Intent(MessageActivity.this,UserInfoSettingsActivity.class);
                intent.putExtra("friend",friend);
                intent.putExtra("user",userItem);
                startActivity(intent);

            }
        });
    }
    private void gallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }
    private void requestStoragePermission() {

        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.e("TAG","开始" + hasCameraPermission);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED){
            // 拥有权限，可以执行涉及到存储权限的操作
            Log.e("TAG", "你已经授权了该组权限");
        }else {
            // 没有权限，向用户申请该权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("TAG", "向用户申请该组权限");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            }
        }

    }

    /**
     * 动态申请权限的结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 用户同意，执行相应操作
                Log.e("TAG","用户已经同意了存储权限");
            }else {
                // 用户不同意，向用户展示该权限作用
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//      super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {// 操作成功了

            switch (requestCode) {

                case REQUEST_CODE_GALLERY:// 图库选择图片

                    uri = data.getData();// 获取图片的uri
                    if (uri.getPath().length() > 0) {
                        /*Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            new fileUnit().upLoadMessageImage(uri, this, userItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        new fileUnit().upLoadMessageImage(uri, this, userItem);
                    }
            }

        }
    }

    protected void  showAcceptDialog()
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.friend_dialog);
        alterDiaglog.setMessage("Do you want to accept the invitation from "+friend.getUserName()+"?");

        alterDiaglog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MessageActivity.this, "Click Add", Toast.LENGTH_SHORT).show();
                addFriends(friend);
            }
        });
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MessageActivity.this,"Click Continue",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alterDiaglog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        alterDiaglog.show();
    }


    protected void addFriends(final Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        friend.setType(true);
        db.collection("UserList").document(userItem.getEmail()).collection("FriendList").document(friend.getEmail())
                .set(friend)
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

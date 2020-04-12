package com.example.larkmessage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.MainActivity;
import com.example.larkmessage.R;
import com.example.larkmessage.adapter.FriendAdapter;
import com.example.larkmessage.adapter.MessageAdapter;
import com.example.larkmessage.entity.Chat;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.DateUnit;
import com.example.larkmessage.unit.ValidationUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {


    private DateUnit u =new DateUnit();
    private  UserItem userItem ;
    private Friend friend;
    private  EditText et;
    private FriendAdapter friendAdapter;
    private RecyclerView recyclerView;
    ArrayList<String> onlineList = new ArrayList<String>();
    ArrayList<Friend> list = null;
    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userItem =((MainActivity)getActivity()).getUserItem();
        if(userItem.getBgColor()!=null)
        view.setBackgroundColor(((MainActivity)getActivity()).getUserItem().getBgColor());
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFriendsDialog();
            }
        });
        recyclerView = view.findViewById(R.id.friend_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        friendAdapter =new FriendAdapter(getActivity(),userItem);
        recyclerView.setAdapter(friendAdapter);
        ListenFriends();
        //listentoOnline();
    }
/*
    public void listentoOnline()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("RecordInfo").document("OnlineRecord")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        getOnline();
                    }
                });
    }

    public void getOnline()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("RecordInfo").document("OnlineRecord")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                onlineList = (ArrayList<String>) documentSnapshot.getData().get("OnlineList");
                Toast.makeText(getContext(), onlineList.toString(), Toast.LENGTH_SHORT).show();
                upDateStatus();
            }
        });
    }
    public void upDateStatus()
    {
        if(list==null)return;
        for(Friend item:list)
        {
            if(onlineList.contains(item.getEmail()))
            {
                item.setStatus(true);
            }
            else
            {
                item.setStatus(false);
            }
        }
        friendAdapter.addAll(list);
    }*/
    public void upDate()
    {
        list =new ArrayList<Friend>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(userItem.getEmail()).collection("FriendList")
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
                                        list.add(documentSnapshot.toObject(Friend.class));

                                    }
                                    friendAdapter.addAll(list);
                                    //getOnline();
                                }
                                else
                                {
                                    Log.w("tag", "error getting document ", task.getException());
                                }
                            }
                        }
                );
    }
    public void ListenFriends()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query =db.collection("UserList").document(userItem.getEmail()).collection("FriendList");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("listener error", "listen:error", e);
                    return;
                }

                upDate();
            }
        });
    }


    protected void  showAddFriendsDialog()
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(getContext());
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.friend_dialog);
        alterDiaglog.setMessage(R.string.please_input_your_friendemail);
        et=new EditText(getActivity());
        et.setSingleLine();
        et.setHint(R.string.email_address);
        alterDiaglog.setView(et);
        alterDiaglog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.click_add,Toast.LENGTH_SHORT).show();
                friend =new Friend();
                friend.setEmail(et.getText().toString());
                try {
                    friend.setDefault();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(ValidationUnit.checkEmail(friend.getEmail())) {
                    if(friend.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())!=true)
                    {
                    checkExistFriends(friend);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.dont_add_yourself,Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(getContext(), R.string.input_is_empty,Toast.LENGTH_SHORT).show();
            }
        });
        alterDiaglog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.click_cancel,Toast.LENGTH_SHORT).show();

            }
        });
        alterDiaglog.setNeutralButton(R.string.moment_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.click_continue,Toast.LENGTH_SHORT).show();
            }
        });
        alterDiaglog.show();
    }

    protected  void checkFriend(final Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       // check the user exist and call addfriends();
        db.collection("UserList")
                .whereEqualTo("email", friend.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty())
                            {
                                Toast.makeText(getContext(), R.string.invaluesd_user,Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                               QuerySnapshot snapshot = task.getResult();
                               DocumentSnapshot documentSnapshot =snapshot.getDocuments().get(0);
                               Map<String ,Object> m = documentSnapshot.getData();
                               friend.setUserName(m.get("userName").toString());
                               if(m.containsKey("icon"))friend.setIcon(Integer.parseInt(m.get("icon").toString()));
                               else friend.setIcon(R.drawable.nn7);
                                CreateChatting(friend);
                            }

                        } else {
                            Toast.makeText(getContext(), R.string.internet_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    protected void checkExistFriends(final Friend friend)
    {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("UserList").document(userItem.getEmail()).collection("FriendList")
                    .whereEqualTo("email",friend.getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty())
                            checkFriend(friend);
                            else {
                                showAddedDialog();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
    }
    protected void addFriends(final Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        friend.setType(true);
        friend.setLetHimAccessMoment(true);
        friend.setStatus(true);
        db.collection("UserList").document(userItem.getEmail()).collection("FriendList").document(friend.getEmail())
                .set(friend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        addYouToYourFriend(friend);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    protected  void addYouToYourFriend( Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Friend yourself = new Friend();
        try {
            yourself.setDefault();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        yourself.setEmail(userItem.getEmail());
        yourself.setIcon(userItem.getIcon());
        yourself.setUserName(userItem.getUserName());
        yourself.setMessageId(friend.getMessageId());
        yourself.setLetHimAccessMoment(false);
        yourself.setStatus(false);
        yourself.setType(false);
        db.collection("UserList").document(friend.getEmail()).collection("FriendList").document(userItem.getEmail())
                .set(yourself)
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
    protected void CreateChatting(final Friend friend)  {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Chat chat =new Chat();
        chat.setChattingName(null);
        try {
            chat.setCreateDate(DateUnit.getSystemTimeAndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> userlist = new ArrayList<>();
        userlist.add(userItem.getEmail());
        userlist.add(friend.getEmail());
        chat.setEmailList(userlist);
        db.collection("Chatting")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        friend.setMessageId(documentReference.getId());
                        addFriends(friend);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    protected void  showAddedDialog()
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(getContext());
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.added_friends);
        alterDiaglog.setMessage(R.string.you_have_added);
        alterDiaglog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.show();
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
        //list =new ArrayList<Friend>();
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

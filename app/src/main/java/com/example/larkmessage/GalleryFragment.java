package com.example.larkmessage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.MainActivity;
import com.example.larkmessage.R;
import com.example.larkmessage.adapter.FriendAdapter;
import com.example.larkmessage.adapter.MomentAdapter;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.Moment;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.fileUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {
    private MomentAdapter Adapter;
    private RecyclerView recyclerView;

    private UserItem userItem;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userItem =((MainActivity)getActivity()).getUserItem();

        updateUI(view);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(),MomentActivity.class);
               intent.putExtra("user",userItem);
               startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.moment_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        Adapter =new MomentAdapter(getActivity(),userItem);
        recyclerView.setAdapter(Adapter);
        listenMoment();
    }

    protected  void getMoment()
    {
        final ArrayList<Moment> list =new ArrayList<Moment>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MomentList")
                .whereEqualTo("finish",true)
                .whereArrayContains("userList",userItem.getEmail())
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
                                        Moment moment =documentSnapshot.toObject(Moment.class);
                                        moment.setPath(documentSnapshot.getId());
                                        list.add(moment);
                                    }
                                    Log.w("error", "error moment"+list.size(), task.getException());
                                    Adapter.addAll(list);
                                }
                                else
                                {
                                    Log.w("tag", "error getting document ", task.getException());
                                }
                            }
                        }
                );

    }
    protected  void listenMoment()
    {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query =db.collection("MomentList")
                    .whereEqualTo("finish",true)
                    .whereArrayContains("userList",userItem.getEmail());

            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("listener error", "listen:error", e);
                        return;
                    }
                    getMoment();
                }
            });
    }
    public  void updateUI(View view)
    {
        if(((MainActivity)getActivity()).getUserItem().getBgColor()!=null)
            view.setBackgroundColor(((MainActivity)getActivity()).getUserItem().getBgColor());
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

    }
}

package com.example.larkmessage.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larkmessage.MessageActivity;
import com.example.larkmessage.MomentActivity;
import com.example.larkmessage.R;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Moment;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.IconDB;
import com.example.larkmessage.unit.fileUnit;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentHolder>{

    private static List<Moment> list = new ArrayList<Moment>();
    private Context context;
    private RecyclerView mRecyclerView;
    private UserItem userItem;
    private IconDB iconDB = new IconDB();

    public MomentAdapter(Context context, UserItem userItem)
    {
        this.context =context;
        this.userItem =userItem;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView =recyclerView;
    }
    @NonNull
    @Override
    public MomentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_view,parent,false);
        return new MomentAdapter.MomentHolder(itemView);
    }
    public void  addAll(ArrayList<Moment> itemList)
    {
        list =  itemList;
        notifyDataSetChanged();
        if(list.size()>0)mRecyclerView.getLayoutManager().scrollToPosition( list.size() );
    }
    protected void agree(Moment moment)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MomentList").document(moment.getPath())
                .update("agree",moment.getAgree())
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
    protected void disagree(Moment moment)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MomentList").document(moment.getPath())
                .update("disagree",moment.getDisagree())
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
    protected  void deleteMoment(Moment moment)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MomentList").document(moment.getPath())
                .delete()
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
    protected void IgnoreMoment(Moment moment)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MomentList").document(moment.getPath())
                .update("userList",moment.getUserList())
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
    protected void  showAgreeDialog(final Moment moment)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Who agree with you?");
        alterDiaglog.setMessage(moment.getAgree().toString());
        alterDiaglog.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.show();
    }
    protected void  showDisagreeDialog(final Moment moment)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Who disagree with you?");
        alterDiaglog.setMessage(moment.getDisagree().toString());
        alterDiaglog.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.show();
    }
    protected void  showDeleteDialog(final Moment moment)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Delete this Moment");
        alterDiaglog.setMessage("Are you sure to delete this moment?");
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMoment(moment);
            }
        });
        alterDiaglog.show();
    }
    protected void  showIgnoreDialog(final Moment moment)
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Delete this Moment");
        alterDiaglog.setMessage("you are not the sender of the moment,are you sure to ignore this moment? If you do that , you will not see this moment forever!");
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               ArrayList<String> newUserList = moment.getUserList();
               newUserList.remove(userItem.getEmail());
               moment.setUserList(newUserList);
                IgnoreMoment(moment);
            }
        });
        alterDiaglog.show();
    }

    @Override
    public void onBindViewHolder(@NonNull MomentHolder holder, final int position) {
        holder.userName .setText(list.get(position).getUserName());
        holder.context.setText(list.get(position).getText());

        if(iconDB.containKey(userItem.getIcon()))
        {
            holder.icon.setImageResource(iconDB.getIconID(userItem.getIcon()));
        }
        else if(iconDB.containValue(userItem.getIcon()))
        {
            holder.icon.setImageResource(userItem.getIcon());
        }
        else
        {
            holder.icon.setImageResource(iconDB.getDefaultIcon());
        }
        final Integer agree = list.get(position).getAgree().size();
        final Integer disagree = list.get(position).getDisagree().size();
        holder.agreeText.setText(agree.toString());
        holder.disagreeText.setText(disagree.toString());

        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Moment moment = list.get(position);
                    ArrayList<String> agreeList = moment.getAgree();
                      ArrayList<String> disagreeList = moment.getDisagree();
                    if(disagreeList==null) disagreeList = new ArrayList<String>();
                    if(agreeList==null) agreeList = new ArrayList<String>();
               // Toast.makeText(context, agreeList.toString(), Toast.LENGTH_SHORT).show();
                if(disagreeList.contains(userItem.getEmail())){
                    disagreeList.remove(userItem.getEmail());
                    moment.setDisagree(disagreeList);
                    disagree(moment);
                };
                    if(agreeList.contains(userItem.getEmail()))
                    {
                        agreeList.remove(userItem.getEmail());
                        moment.setAgree(agreeList);
                        agree(moment);
                    }
                    else{
                    agreeList.add(userItem.getEmail());
                    moment.setAgree(agreeList);
                    agree(moment);
                }

            }
        });
        holder.disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Moment moment = list.get(position);
                ArrayList<String> agreeList = moment.getAgree();
                ArrayList<String> disagreeList = moment.getDisagree();
                if(disagreeList==null) disagreeList = new ArrayList<String>();
                if(agreeList==null) agreeList = new ArrayList<String>();

                if(agreeList.contains(userItem.getEmail())){
                    agreeList.remove(userItem.getEmail());
                    moment.setAgree(agreeList);
                    agree(moment);
                };
                if(disagreeList.contains(userItem.getEmail())){
                    disagreeList.remove(userItem.getEmail());
                    moment.setDisagree(disagreeList);
                    disagree(moment);
                }
                else {
                    disagreeList.add(userItem.getEmail());
                    moment.setDisagree(disagreeList);
                    disagree(moment);
                }
            }
        });
       // Bitmap bm = BitmapFactory.decodeFile(list.get(position).getImage());
        //holder.image.setImageBitmap(bm);
        try {
            new fileUnit().downloadImage(list.get(position),holder.image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(list.get(position).getPath().contains(userItem.getEmail()))
                {
                    showDeleteDialog(list.get(position));
                }
                else
                {
                    showIgnoreDialog(list.get(position));
                }

                return false;
            }
        });
        holder.agreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgreeDialog(list.get(position));
            }
        });
        holder.disagreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisagreeDialog(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MomentHolder extends RecyclerView.ViewHolder
    {
        private CardView card;
        private ImageView icon;
        private ImageView image;
        private ImageButton agree;
        private ImageButton disagree;
        private TextView agreeText;
        private TextView disagreeText;
        private TextView context;
        private TextView userName;

        public MomentHolder(@NonNull View itemView) {
            super(itemView);

            card =itemView.findViewById(R.id.moment_card);
            icon = itemView.findViewById(R.id.moment_icon);
            image = itemView.findViewById(R.id.moment_image);
            agree = itemView.findViewById(R.id.moment_agree);
            disagree =itemView.findViewById(R.id.moment_disagree);
            context =itemView.findViewById(R.id.moment_context);
            userName =itemView.findViewById(R.id.moment_name);
            agreeText =itemView.findViewById(R.id.agree_textView);
            disagreeText = itemView.findViewById(R.id.disagree_textView);
        }
    }
}

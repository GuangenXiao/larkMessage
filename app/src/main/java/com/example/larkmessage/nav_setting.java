package com.example.larkmessage;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larkmessage.adapter.IconAdapter;
import com.example.larkmessage.adapter.MessageAdapter;
import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.IconDB;
import com.example.larkmessage.unit.loginUnit;
import com.example.larkmessage.unit.userUnit;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nav_setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nav_setting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mCurrentBackgroundColor = Color.WHITE;
    private Button button ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private IconAdapter Adapter;
    private RecyclerView recyclerView;
    public nav_setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nav_setting.
     */
    // TODO: Rename and change types and number of parameters
    public static nav_setting newInstance(String param1, String param2) {
        nav_setting fragment = new nav_setting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_nav_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button =view.findViewById(R.id.color_button);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        updateUI();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showColorDialog();

            }
        });
    }
    public  void updateUI()
    {
        if(((MainActivity)getActivity()).getUserItem().getBgColor()!=null)
            view.setBackgroundColor(((MainActivity)getActivity()).getUserItem().getBgColor());
    }
    protected void  showIconDialog()
    {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(getContext());
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle(R.string.choose_your_icon);
        //recyclerView = getView().findViewById(R.id.icon_recycler);
        recyclerView = new RecyclerView(getContext());
        if(recyclerView==null)return;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);
        Adapter =new IconAdapter(new UserItem(),getContext());
        Adapter.onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(Adapter);
        Adapter.addAll(new IconDB().getList());
        alterDiaglog.setView(recyclerView);
        alterDiaglog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.show();
    }
    protected void showColorDialog() {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(getString(R.string.choose_color))
                .initialColor(mCurrentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(getString(R.string.ok), new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        // changeBackgroundColor(selectedColor);
                        UserItem u =new UserItem();
                        u.setBgColor(selectedColor);
                        UserItem userItem =((MainActivity)getActivity()).getUserItem();
                        if(userItem!=null) {userItem.setBgColor(selectedColor);
                            ((MainActivity)getActivity()).setUserItem(userItem);
                        }
                        updateUI();
                        new userUnit().changeUserInfo(u);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}

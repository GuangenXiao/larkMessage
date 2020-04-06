package com.example.larkmessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.larkmessage.MainActivity;
import com.example.larkmessage.R;
import com.example.larkmessage.unit.loginUnit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SlideshowFragment extends Fragment {

    private Button logoutButton;
    private loginUnit loginunit ;
    private TextView navMailTextView;
    private TextView navNameTextView;
    private ImageView navIconImageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginunit =new loginUnit();
        logoutButton =view.findViewById(R.id.logout_button);
        updateUI(view);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user !=null) {
                    loginunit.logoutFcn(getActivity());
                }
                else
                {
                    loginunit.logoutFcn(getActivity());
                }
            }
        });
    }

    public  void updateUI(View view)
    {
        if(((MainActivity)getActivity()).getUserItem().getBgColor()!=null)
            view.setBackgroundColor(((MainActivity)getActivity()).getUserItem().getBgColor());
    }

}

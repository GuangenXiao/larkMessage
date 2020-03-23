package com.example.larkmessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.larkmessage.MainActivity;
import com.example.larkmessage.R;

public class GalleryFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI(view);
    }

    public  void updateUI(View view)
    {
        if(((MainActivity)getActivity()).getUserItem().getBgColor()!=null)
            view.setBackgroundColor(((MainActivity)getActivity()).getUserItem().getBgColor());
    }
}

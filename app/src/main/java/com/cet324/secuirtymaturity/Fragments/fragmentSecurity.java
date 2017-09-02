package com.cet324.secuirtymaturity.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.R;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class fragmentSecurity extends Fragment {
    View myView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Security Check");
        myView = inflater.inflate(R.layout.fragment_help, container, false);
        return myView;
    }
    //create method that takes in a target and a password to compare with user input before sending to desired location or returning home
}

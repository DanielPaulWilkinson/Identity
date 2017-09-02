package com.cet324.secuirtymaturity.Fragments;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.Database.DBHelper;
import com.cet324.secuirtymaturity.R;


/**
 * Created by DanielWilkinson on 12/23/2016.
 */
public class fragmentHome extends Fragment {
   private View myView;
   private String email;
   private TextView txtWelcome, txtPasswords;
   private SharedPreferences sharedPreferences;
   private DBHelper db;
   private ImageView imgSettings, imgHelp, imgCreate, imgAll;



    //logout option, make these activities so that the menu cannot appear.


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Home");
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        return myView;
    }
    public Boolean isLoggedIn(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        String email= sharedPreferences.getString("Email", "");
        if(email.equals("") || email.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!isLoggedIn()){
            Fragment fragment = new fragmentLogin();
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }


        db = new DBHelper(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        email = getStringFromPreferences(sharedPreferences,"Email","");
        txtWelcome = (TextView) myView.findViewById(R.id.txtWelcome);
        txtPasswords = (TextView) myView.findViewById(R.id.txtPasswords);
        imgAll = (ImageView) myView.findViewById(R.id.imgPasswords);
        imgCreate = (ImageView) myView.findViewById(R.id.imgCreator);
        imgHelp = (ImageView)myView.findViewById(R.id.imgHelp);
        imgSettings = (ImageView) myView.findViewById(R.id.imgSettings);
        imgAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new fragmentAllPasswords();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });
        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new fragmentSettings();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });
        imgCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new fragmentPasswordCreator();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new fragmentHelp();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });

        txtWelcome.setText(email);
        txtPasswords.setText(String.valueOf(db.getPasswordCount(email)));
    }
    public static String getStringFromPreferences(SharedPreferences shared ,String key, String defaultValue) {
        String temp = shared.getString(key,defaultValue);
        return temp;
    }

}

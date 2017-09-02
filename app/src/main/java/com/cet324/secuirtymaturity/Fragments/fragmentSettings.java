package com.cet324.secuirtymaturity.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.Database.DBHelper;
import com.cet324.secuirtymaturity.ObjectClasses.User;
import com.cet324.secuirtymaturity.R;

import java.util.regex.Pattern;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class fragmentSettings extends Fragment {
    private View myView;
    private Button btnSubmitEmail;
    private EditText etold, etnew, etretype;
    private DBHelper    db;
    private SharedPreferences sharedPreferences;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Settings");
        db = new DBHelper(getContext());
        myView = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
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
        btnSubmitEmail = (Button) myView.findViewById(R.id.btnSubmitEmail);
        etold = (EditText)myView.findViewById(R.id.txtOld);
        etnew = (EditText)myView.findViewById(R.id.txtNew);
        etretype = (EditText)myView.findViewById(R.id.txtretype);
        btnSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the user
                User user = getUser(getUserEmail());
                //get all strings from text field
                String old = etold.getText().toString().trim();
                String New = etnew.getText().toString().trim();
                String retype = etretype.getText().toString().trim();



                //if they are all filled in
                if(!CheckStringEmpty(old,etold,"required") || !CheckStringEmpty(New,etnew,"required") || !CheckStringEmpty(retype,etretype,"required")){
                    //if they are all valid emails
                    if(isValidEmaillId(old) || isValidEmaillId(New) || isValidEmaillId(retype)) {
                        //if old email is equal to the current user email
                        if (old.equals(user.getEmail())) {
                            //if any of new emails entered do not exist already continue...
                            if(CheckForEmailExistance(New) == null || CheckForEmailExistance(retype) == null) {
                                //if the fields aren't empty and new emails match
                                if (!old.isEmpty() && !New.isEmpty() && New.equals(retype)) {
                                    if(isValidEmaillId(New)) {
                                        //create new user
                                        User users = new User(New, user.getPass(), user.getKey(),user.getSalt(),user.getOriginal());
                                        //update this with olf id
                                        db.updateUser(users, db.getUserID(old));
                                        //alert user of victory.
                                        Toast.makeText(getContext(), "Updated Email", Toast.LENGTH_LONG).show();
                                        //else show error toasts...
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("Email", New);
                                        editor.apply();
                                    } else{
                                        Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "New email does not match", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "Email Exists", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getContext(), "Incorrect current email", Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_LONG).show();

                    }
                }
                else{
                }

            }
        });
    }
    //is valid email REGEX
    private boolean isValidEmaillId(String email) {
        //using the custom regex builder i made a full working email validator that matches the string entered.
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }



    public User getUser(String email) {
        User user = db.getUser(email);
        return user;
    }
    public String CheckForEmailExistance(String email){
        User user = db.getUser(email);
        if(user != null){
            return user.getEmail();
        }
        else{
            return null;
        }

    }
    public String getUserEmail() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");
        if (!email.isEmpty()) {
            return email;
        } else {
            return null;
        }
    }public Boolean CheckStringEmpty(String text, EditText et, String message) {
        if (TextUtils.isEmpty(text)) {
            et.setError(message);
            return true;
        } else {
            return false;
        }
    }
}

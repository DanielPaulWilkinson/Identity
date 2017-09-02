package com.cet324.secuirtymaturity.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.cet324.secuirtymaturity.PasswordSecurity.Encryption;
import com.cet324.secuirtymaturity.R;

import java.util.regex.Pattern;

/**
 * Created by DanielWilkinson on 2/7/2017.
 */

public class fragmentRegister extends Fragment{
    private View myView;
    private EditText email, pass1, pass2, encryptionKey, encryptionKey2;
    private Button btnContinue;
    private DBHelper db;
    private Encryption en;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Register");
        myView = inflater.inflate(R.layout.activity_register, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
//add the btnContinue click listener

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if fields are empty remind user
                String emailtext = email.getText().toString().trim();
                String pass1text = pass1.getText().toString().trim();
                String pass2text = pass2.getText().toString().trim();
                String key1text = encryptionKey.getText().toString().trim();
                String key2text = encryptionKey2.getText().toString().trim();
                //show error if empty

                if (!emailtext.equals("")) {
                    if (!pass1text.equals("")) {
                        if (!pass2text.equals("")) {
                            if (!key1text.equals("")) {
                                if (!key2text.equals("")) {
                                    //if the email does not exist
                                    if (getUserEmail() == null) {
                                        //if passwords don't match remind user
                                        if (!pass1text.isEmpty() && !pass2text.isEmpty() && pass1text.equals(pass2text)) {
                                            if (!key1text.isEmpty() && !key2text.isEmpty() && key1text.equals(key2text)) {
                                                if (!pass1text.isEmpty() && !pass2text.isEmpty() && !emailtext.isEmpty() &&
                                                        !key1text.isEmpty() && !key2text.isEmpty() && pass1text.equals(pass2text) &&
                                                        key1text.equals(key2text)) {
                                                    if (isValidEmaillId(emailtext)) {
                                                        if (key1text.length() >= 18 || key2text.length() >= 18) {
                                                            String salt = String.valueOf(en.getSalt());
                                                            User user = new User(emailtext, en.makeUnusable(pass1text, key1text, salt), key1text, salt, pass1text);
                                                            db.addUser(user);
                                                            Fragment fragment = new fragmentLogin();
                                                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                            fragmentTransaction.addToBackStack(null);
                                                            fragmentTransaction.replace(R.id.content_frame, fragment);
                                                            fragmentTransaction.commit();

                                                        } else {
                                                            Toast.makeText(getContext(), "Uh-oh, key required to be 18 characters", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getContext(), "Uh-oh, incorrect email format.", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Uh-oh, keys don't match.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Uh-oh, passwords don't match.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Uh-oh, we can't find you.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    CheckStringEmpty(key2text, encryptionKey2, "Required");
                                }
                            } else {
                                CheckStringEmpty(key1text, encryptionKey, "Required");
                            }

                        } else {
                            CheckStringEmpty(pass2text, pass2, "Required");
                        }
                    } else {

                        CheckStringEmpty(pass1text, pass1, "Required");
                    }
                } else {
                    CheckStringEmpty(emailtext, email, "Required");
                }
            }

                        });
                    }



    private void CheckStringEmpty(String text, EditText et, String message) {
        if (TextUtils.isEmpty(text)) {
            et.setError(message);
            return;
        }
    }


    private void init() {
        //get all fields within the view
        email = (EditText) myView.findViewById(R.id.txtEmail);
        pass1 = (EditText) myView.findViewById(R.id.txtPass1);
        pass2 = (EditText) myView.findViewById(R.id.txtPass2);
        encryptionKey = (EditText) myView.findViewById(R.id.txtKey);
        encryptionKey2 = (EditText) myView.findViewById(R.id.txtKey2);

        btnContinue = (Button) myView.findViewById(R.id.btnContinue);
        //reference database within method
        db = new DBHelper(getContext());
        en = new Encryption();
    }
    private String getUserEmail(){
        User user = db.getUser(email.getText().toString().trim());
        if(user != null){
            return user.getEmail();
        }
        else{
            return null;
        }

    }
    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}




package com.cet324.secuirtymaturity.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

/**
 * Created by DanielWilkinson on 2/7/2017.
 */

public class fragmentLogin extends Fragment {
    View myView;
    private EditText txtEmail, txtPassword;
    private Button btnRegister, btnLogin;
    private DBHelper db;
    private SharedPreferences sharedPreferences;
    private Encryption en;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Login");
        myView = inflater.inflate(R.layout.activity_login, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DBHelper(getContext());
        en = new Encryption();
        txtEmail = (EditText) myView.findViewById(R.id.txtLoginEmail);
        txtPassword = (EditText) myView.findViewById(R.id.txtLoginPassword);
        btnLogin = (Button) myView.findViewById(R.id.btnLogin);
        btnRegister = (Button) myView.findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get 2 text fields
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                //try to find user in database
                User user = db.getUser(email);
                //if not empty
                if (!email.isEmpty() && !password.isEmpty()) {
                    //if not null
                    if (user != null) {
                        //get user
                        String key = String.valueOf(user.getKey());
                        String salt = String.valueOf(user.getSalt());
                        //get hashed password from the input password using the same user key and salt
                        String passwordAttempt = en.makeUnusable(password, key, salt);
                        if (passwordAttempt.equals(user.getPass())) {
                            sharedPreferences = getContext().getSharedPreferences("ShaPreferences", getContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Email", user.getEmail());
                            editor.apply();
                            setLoggedin();
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            Fragment fragment = new fragmentPasswordCreator();
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();

                        } else {
                            Toast.makeText(getContext(), "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Cannot locate user", Toast.LENGTH_LONG).show();
                    }
                } else {
                    CheckStringEmpty(email, txtEmail, "Required");
                    CheckStringEmpty(password, txtPassword, "Required");
                }
            }



        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new fragmentRegister();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void CheckStringEmpty(String text, EditText et, String message) {
        if (TextUtils.isEmpty(text)) {
            et.setError(message);
            return;
        }
    }
    public void setLoggedin(){
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem element = menuNav.findItem(R.id.menu_account);
        element.setTitle("Logout");

    }
}

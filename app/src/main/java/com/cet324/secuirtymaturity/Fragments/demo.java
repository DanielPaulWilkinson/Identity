package com.cet324.secuirtymaturity.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.PasswordSecurity.Encryption;
import com.cet324.secuirtymaturity.R;

/**
 * Created by DanielWilkinson on 2/8/2017.
 */

public class demo extends Fragment{
    private View myView;
    private EditText salt,pepper,password, regpass, hashpass, encryptpass;
    private Button resultButton, gen;
    private Encryption en;
    private String saltString;
    private String passwordString;
    private String pepperString;
    private String passwordResult="",passwordResultHashed="",passwordResultEncrypted="";
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Demo Password");
        myView = inflater.inflate(R.layout.demo, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        salt = (EditText)myView.findViewById(R.id.txtSalt);
        pepper = (EditText) myView.findViewById(R.id.txtPepper);
        password = (EditText)myView.findViewById(R.id.txtPassword);
        resultButton = (Button)myView.findViewById(R.id.btnRes);
        regpass = (EditText)myView.findViewById(R.id.result);
        hashpass = (EditText) myView.findViewById(R.id.txtHashed);
        encryptpass = (EditText)myView.findViewById(R.id.txtEncrypted);
        gen = (Button)myView.findViewById(R.id.btngenerate);


        en = new Encryption();
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saltString = String.valueOf(en.getSalt());
                salt.setText(saltString);
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordString = password.getText().toString().trim();
                saltString = salt.getText().toString().trim();
                pepperString = pepper.getText().toString().trim();

                if(!pepperString.isEmpty() || pepperString.equals("")){
                    if(!passwordString.isEmpty() || passwordString.equals("")){
                        if(!saltString.isEmpty() || saltString.equals("")){
                                //get the summarised password;
                                passwordResult = pepperString+passwordString+saltString;
                            Log.d("password 1: ",passwordString);
                            Log.d("password 2: ","Password with pepper and salt: " + passwordResult);

                            //get hashed password
                                passwordResultHashed = en.sha256(passwordResult);
                            Log.d("Password 3: ","Password hashed: "+passwordResultHashed);

                            //get encrypted password
                            passwordResultEncrypted = en.makeUnusable(passwordResultHashed,"qqqqqqqqqqqqqqqqqq",saltString);
                            Log.d("Password 4: ","Password encrypted: "+passwordResultEncrypted);

                            //show password
                                regpass.setText(passwordResult+saltString);
                                hashpass.setText(passwordResultHashed);
                                encryptpass.setText(passwordResultEncrypted);

                        }else{
                            Toast.makeText(getContext(),"Please enter or generate a salt",Toast.LENGTH_LONG).show();
                        }


                    }else{
                        Toast.makeText(getContext(),"Please enter a password",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getContext(),"Please enter a pepper",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}

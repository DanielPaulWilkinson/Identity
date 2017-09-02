package com.cet324.secuirtymaturity.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.Database.DBHelper;
import com.cet324.secuirtymaturity.PasswordSecurity.Encryption;
import com.cet324.secuirtymaturity.ObjectClasses.Password;
import com.cet324.secuirtymaturity.ObjectClasses.User;
import com.cet324.secuirtymaturity.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class fragmentAllPasswordDetail extends Fragment {
   private View myView;
   private DBHelper db;
   private String passwordID;
   private TextView txtDate, txtPassordEncrypted, txtPasswordDecoded, txtUpdateDate;
   private Button btnDecode;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_detail, container, false);
        db = new DBHelper(getContext());
        return myView;
    }

        @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtDate = (TextView)myView.findViewById(R.id.txtDateCreated);
        txtPassordEncrypted = (TextView) myView.findViewById(R.id.txtEncrypted);
        txtPasswordDecoded = (TextView)myView.findViewById(R.id.txtdecoded);
        txtUpdateDate = (TextView)myView.findViewById(R.id.txtUpdateDate);
        btnDecode = (Button) myView.findViewById(R.id.btnDecode);
        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater=LayoutInflater.from(getContext());
                View layout=inflater.inflate(R.layout.fragment_security,null);
                builder.setView(layout);
                final EditText userpasswordinput=(EditText)layout.findViewById(R.id.txtPass);
                builder.setCancelable(false)
                        .setMessage("Please Enter Encryption Key to continue...")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(keysEqual(userpasswordinput.getText().toString(),getCurrentUser().getKey()) &&
                                        !userpasswordinput.getText().toString().trim().isEmpty()){
                                    try {
                                        Encryption en = new Encryption();
                                        String decoded =  en.decode(db.getPassword(passwordID).getName(),getCurrentUser().getKey());
                                        txtPasswordDecoded.setText(decoded);

                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    } catch (InvalidKeySpecException e) {
                                        e.printStackTrace();
                                    } catch (InvalidKeyException e) {
                                        e.printStackTrace();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchPaddingException e) {
                                        e.printStackTrace();
                                    } catch (BadPaddingException e) {
                                        e.printStackTrace();
                                    } catch (IllegalBlockSizeException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    Toast.makeText(getContext(),"Incorrect Encryption Key",Toast.LENGTH_LONG).show();

                                }
                            }
                        })
                        //when a negative button is clicked ie the update button
                        .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragment = new fragmentPasswordCreator();
                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.commit();
                            }
                        }).show();
            }
        });


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            passwordID = String.valueOf(bundle.get("ID"));
            SetCurrentPassword(passwordID);
        }

    }
    private static boolean keysEqual(String key1, String key2) {
        if (key1.equals(key2)) {
            return true;
        }
        return false;
    }

    public void CheckString(EditText et, String message){
        et.setError(message);
        return;
    }

    private void SetCurrentPassword(String passwordID) {
        Password password = db.getPassword(passwordID);
        txtPassordEncrypted.setText(password.getName());
        txtDate.setText(password.getDate());
        txtUpdateDate.setText(password.getFrequencyOfChange());

        ((MainActivity) getActivity())
                .setActionBarTitle("Password For: "+password.getCompany());

    }
    private User getCurrentUser(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "No Email");
        User user = db.getUser(email);
        if (user != null){
            return user;
        }
        else{
            return null;
        }
    }
}

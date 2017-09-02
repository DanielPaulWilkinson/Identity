package com.cet324.secuirtymaturity.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by DanielWilkinson on 1/2/2017.
 */


//add passwords relating to user id use query


public class fragmentAddPassword extends Fragment {
   private View myView;
   private EditText txtPassword, txtDate, txtCompany;
   private String password, UpdateDate;
   private Button btnSubmitPassword;
   private int year, month, day;
   private Spinner spinnerFrequency;
   private DBHelper db;
   private ArrayList values;
   private ArrayAdapter<String> adapter;
   private Encryption en;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Add Password");
        myView = inflater.inflate(R.layout.fragment_add_password, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        en = new Encryption();
        txtCompany = (EditText) myView.findViewById(R.id.txtAddFor);//company
        txtPassword = (EditText) myView.findViewById(R.id.txtAddPassword);//actual password
        txtDate = (EditText) myView.findViewById(R.id.txtAddDate);//current date
        btnSubmitPassword = (Button) myView.findViewById(R.id.btnSumbitPassword);
        spinnerFrequency = (Spinner) myView.findViewById(R.id.spinnerFrequencyofchange);
        values = new ArrayList();
        values.add("2 Weeks");
        values.add("4 Weeks");
        values.add("8 Weeks");
        //make array the adapter to the spinner.
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerFrequency.setAdapter(adapter);
        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpdateDate = spinnerFrequency.getSelectedItem().toString().trim();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        db = new DBHelper(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.fragment_security, null);
        builder.setView(layout);
        final EditText userpasswordinput = (EditText) layout.findViewById(R.id.txtPass);
        builder.setCancelable(false)
                .setMessage("Please Enter Password to continue...")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user = db.getUser(getCurrentUserEmail());
                        String key = String.valueOf(user.getKey());
                        String salt = String.valueOf(user.getSalt());
                        String passwordAttempt = en.makeUnusable(userpasswordinput.getText().toString().trim(), key, salt);

                       if(user.getPass().equals(passwordAttempt)){

                               Toast.makeText(getContext(), "Authenticated", Toast.LENGTH_LONG).show();

                           }
                         else {
                            CheckString(userpasswordinput, "Wrong Password");
                            Fragment fragment = new fragmentPasswordCreator();
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
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
                })
                .show();
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            password = String.valueOf(bundle.get("Password"));
            txtPassword.setText(password);
        }
        btnSubmitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckStringEmpty(txtCompany.getText().toString(), txtCompany, "Required")
                        && !CheckStringEmpty(txtDate.getText().toString(), txtDate, "Required")
                        && !CheckStringEmpty(txtPassword.getText().toString(), txtPassword, "Required")) {
                    if (!CheckSpinnerEmpty(spinnerFrequency.getSelectedItem().toString(), spinnerFrequency, "Required")) {
                        try {
                            Encryption en = new Encryption();
                            User user = db.getUser(String.valueOf(getCurrentUserEmail()));
                            String encoded = null;
                            encoded = en.encode(txtPassword.getText().toString().trim(), user.getKey());
                            Password password = new Password(encoded, txtCompany.getText().toString().trim(), txtDate.getText().toString().trim(), getCurrentUserEmail(), UpdateDate);
                            db.addPassword(password);
                            emptyInputs();
                            Toast.makeText(getContext(), "Added New Password", Toast.LENGTH_LONG).show();
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
                    } else {
                        Toast.makeText(getContext(), "Cannot Add New Password need  frequency of change", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Cannot Add New Password", Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    public void emptyInputs() {
        txtCompany.setText("");
        txtPassword.setText("");
        txtDate.setText("");
    }

    private void showDate(int year, int month, int day) {
        txtDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void CheckString(EditText et, String message) {
        et.setError(message);
        return;
    }

    public Boolean CheckStringEmpty(String text, EditText et, String message) {
        if (TextUtils.isEmpty(text)) {
            et.setError(message);
            return true;
        } else {
            return false;
        }
    }

    public Boolean CheckSpinnerEmpty(String text, Spinner et, String message) {
        if (TextUtils.isEmpty(text)) {
            TextView errorText = (TextView) spinnerFrequency.getSelectedView();
            errorText.setError("Please select an update rate!");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("my actual error text");//changes the selected item text to this
            return true;
        } else {
            return false;
        }

    }

    public String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "No Email");
        if (email != null) {
            return email;
        } else {
            return null;
        }
    }
    private String getCurrentEncodedPassword() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "No Email");
        DBHelper db = new DBHelper(getContext());
        User user = db.getUser(email);
        if (user != null) {
            return user.getPass();
        } else {
            return null;
        }

    }
    //get current date ask for permission
    private String getCurrentUserPassword() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "No Email");
        DBHelper db = new DBHelper(getContext());
        User user = db.getUser(email);
        if (user != null) {
            return user.getPass();
        } else {
            return null;
        }

    }
}

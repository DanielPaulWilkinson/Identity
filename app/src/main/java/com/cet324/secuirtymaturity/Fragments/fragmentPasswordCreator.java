package com.cet324.secuirtymaturity.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
 * Created by DanielWilkinson on 12/23/2016.
 */
public class fragmentPasswordCreator extends Fragment {
    private  View myView;
    private  EditText txtPassword;
    private   TextView txtStrength, txtLength, txtCapital, txtLower, txtSymbol, txtNumbers;
    private  Button btnCheck, btnReset, btnSave, btnmakepassword, btnmakeKey;
    private  int total;
    private  DBHelper db;
    private   TextView txtComment;
    private  Boolean exist;
    private  Encryption en;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Password Creator");
        myView = inflater.inflate(R.layout.fragment_password_creator, container, false);
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
        if(!isLoggedIn()){
            Fragment fragment = new fragmentLogin();
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
        db = new DBHelper(getContext());
        en = new Encryption();
        txtPassword = (EditText) myView.findViewById(R.id.txtPassCreator);
        txtStrength = (TextView) myView.findViewById(R.id.txtStrength);
        txtLength = (TextView) myView.findViewById(R.id.txtCount);
        txtSymbol = (TextView) myView.findViewById(R.id.txtSymbols);
        txtCapital = (TextView) myView.findViewById(R.id.txtCapitals);
        txtLower = (TextView) myView.findViewById(R.id.txtLowercase);
        txtNumbers = (TextView) myView.findViewById(R.id.txtNumbers);
        txtComment = (TextView) myView.findViewById(R.id.txtComment);
        btnCheck = (Button) myView.findViewById(R.id.btnCheck);
        btnReset = (Button) myView.findViewById(R.id.btnReset);
        btnmakeKey = (Button) myView.findViewById(R.id.btnMakeKey);
        btnmakepassword = (Button) myView.findViewById(R.id.btnMakePassword);
        txtComment.setTextColor(Color.parseColor("#ff0000"));
        setFields();


        btnSave = (Button) myView.findViewById(R.id.btnSave);
        btnmakeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                if (userpasswordinput.getText().toString().equals(getCurrentOriginalPassword()) && !userpasswordinput.getText().toString().trim().isEmpty()) {
                                    String password = txtPassword.getText().toString().trim();
                                    User user = getUser(getUserEmail());
                                    if (user != null) {
                                        if (txtPassword.getText().length() >= 18) {




                                            User user2 = new User(user.getEmail(),user.getPass(),password, user.getSalt(),user.getOriginal());
                                            String id = db.getUserID(getUserEmail());
                                            db.updateUser(user2, id);


                                            Toast.makeText(getContext(), "Updated Key", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(getContext(), "Key needs to be 18 characters long", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Cannot update: unknown user", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Toast.makeText(getContext(), "Passwords Don't Match", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        //when a negative button is clicked ie the update button
                        .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });
        //button to update the users password...
        btnmakepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create a messsage box to appear
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //get the layout from context
                LayoutInflater inflater = LayoutInflater.from(getContext());
                //get the layout fragment security
                View layout = inflater.inflate(R.layout.fragment_security, null);
                //set the view of the layout to the fragment
                builder.setView(layout);
                //get the edit text in the layout
                final EditText userpasswordinput = (EditText) layout.findViewById(R.id.txtPass);
                //make sure the user cannot close this authentication message box
                builder.setCancelable(false)
                        //show message
                        .setMessage("Please Enter Password to continue...")
                        //create buttons
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            //on the click of the continue button
                            public void onClick(DialogInterface dialog, int which) {
                                //on button click get the current text in the field named txtpassword (to string and trim white space)
                                String password = txtPassword.getText().toString().trim();
                                //get the current user with the current email and password.
                                User user = db.getUser(getUserEmail(),getCurrentEncodedPassword());
                                //if the password is empty or null
                                if (!password.isEmpty() && password != null) {
                                    //if the user is found
                                    if (user != null) {
                                        //find the user data
                                        String key = String.valueOf(user.getKey());
                                        String salt = String.valueOf(user.getSalt());
                                        //find the password hashed to unreadable for comparison
                                        String passwordAttempt = en.makeUnusable(userpasswordinput.getText().toString(), key, salt);
                                        String passwordToSave = en.makeUnusable(password, key, salt);
                                        //if the current user password and the entered password match
                                        if (user.getPass().equals(passwordAttempt)) {
                                            //apply hash irreversible
                                            user.setPass(passwordToSave);
                                            //store this for password creator (human readable)
                                            user.setOriginal(password);
                                            //apply changes to database
                                            db.updateUser(user, db.getUserID(getUserEmail()));
                                            //alert user
                                            Toast.makeText(getContext(), "Your password is updated", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(getContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Cannot update: unknown user", Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(getContext(), "Passwords are empty", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        //when a negative button is clicked ie the update button
                        .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the user by email
                User user = db.getUser(getUserEmail());
                if(user != null) {
                    //get the encoded password
                    String encoded = null;
                    try {
                        encoded = en.encode(txtPassword.getText().toString().trim(), user.getKey());
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
                    //check for the passwords existence
                    Boolean passwordExistance = CheckPasswordExistance(encoded);
                    if(passwordExistance){

                            Toast.makeText(getContext(), "Password in use, please create a different password.", Toast.LENGTH_LONG).show();
                    }else{
                        //move the user to the add password page
                        Fragment fragment = new fragmentAddPassword();
                        //create a bundle of data
                        Bundle bundle = new Bundle();
                        //begin to move page
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        //begin transaction
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        //put the password in the bundle for the next page
                        bundle.putString("Password", txtPassword.getText().toString().trim());
                        //add this fragment to the stack so the back button works
                        fragmentTransaction.addToBackStack(null);
                        //ass the password to the fragment
                        fragment.setArguments(bundle);
                        //move page
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.commit();
                    }


                }
                else{
                        Toast.makeText(getContext(), "Cannot Locate User", Toast.LENGTH_LONG).show();

                    }

            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFields();
            }
        });

        //button to show feedback to user for password strength
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //preset scoreand fields to empty/0
                setFields();
                //get password string strip of spaces
                String password = txtPassword.getText().toString().trim();
                //get the total score
                total = CheckForSymbolNumberCapitalLower(password) + CheckForLengthScore(password);
                //display length to user
                txtLength.setText("Character Count: " + CheckForLength(password));
                //check to see if the password is all letters, numbers or symbols
                if (!password.matches("[0-9]+") && password.length() > 2) {
                    if (!password.matches("[A-Z]+") && password.length() > 2) {
                        if (!password.matches("[a-z]+") && password.length() > 2) {
                            //display total strength if strength is >100 make equal to 100.
                            txtStrength.setText("Password Strength: " + calculateScore(total) + "%");
                            //if password is all of one type of character dont bother to do calculations, remind user what to do in this scenario.
                        } else {
                            txtComment.setText("Suggestion, remember a good password has more then just letters, please add numbers and symbols.");
                            txtStrength.setText("Password Strength: 0%");
                        }
                    } else {
                        txtStrength.setText("Password Strength: 0%");
                        txtComment.setText("Suggestion, remember a good password has more then just uppercase letters, please add numbers and symbols.");
                    }
                } else {
                    txtStrength.setText("Password Strength: 0%");
                    txtComment.setText("Suggestion, remember a good password has more then just numbers, please add letters and symbols.");
                }

            }
        });
        txtPassword.setText(getCurrentOriginalPassword());
        btnCheck.performClick();
    }



    private void setFields() {
        txtNumbers.setText("Number(s): " + 0);
        txtSymbol.setText("Symbol(s): " + 0);
        txtLower.setText("Lowercase Letter(s): " + 0);
        txtCapital.setText("Uppercase Letter(s): " + 0);
        txtStrength.setText("Password Strength: " + 0 + "%");
        txtLength.setText("Character Count: " + 0);
        total = 0;
        txtComment.setText("");
    }

    //method to add points for every so many characters used in the staring
    private int CheckForSymbolNumberCapitalLower(String s) {
        //create local variables
        int upperCase = 0;
        int lowerCase = 0;
        int numberCount = 0;
        int specialCharCount = 0;
        //for loop through the string and project into char array
        for (char c : s.toCharArray()) {
            //if statement to get types of characters
                //if it is an upper case char
            if (Character.isUpperCase(c)) {
                //update the text field
                txtCapital.setText("Uppercase Letter(s): " + String.valueOf(upperCase++ + 1));
                //add points to the total variable
                total += 4;
                //do the same with other chars..
            } else if (Character.isLowerCase(c)) {
                txtLower.setText("Lowercase Letter(s): " + String.valueOf(lowerCase++ + 1));
                total += 3;
            } else if (Character.isDigit(c)) {
                txtNumbers.setText("Number(s): " + String.valueOf(numberCount++ + 1));
                total += 6;

            } else {
                txtSymbol.setText("Symbol(s): " + String.valueOf(specialCharCount++ + 1));
                total += 7;
            }

        }
        //return total to the calculation
        return total;
    }

    //method to keep score below 100%
    private int calculateScore(int overall) {
        //if the percentage is over or equal to 100
        if (overall >= 100) {
            //set the score to 100
            overall = 100;
            //return overall score
            return overall;
        }
        return overall;
    }
    //method to calculate length score
    private int CheckForLengthScore(String s) {
        //firstly get the length of the input string
        int characters = s.length();
        //create a local variable to return
        int score;
        //if the length is below 3 set score to 2 points
        if (characters <= 3) {
            score = 4;
            //if the length is between 3 and 6 give 7 points etc...
        } else if (characters >= 3 && characters <= 6) {
            score = 9;
        } else if (characters >= 6 && characters <= 8) {
            score = 11;
        } else if (characters >= 8 && characters <= 10) {
            score = 17;
        } else {
            score = 20;
        }
        return score;
    }
    //simply just to return length
    private int CheckForLength(String s) {
        return s.length();
    }


    public User getUser(String email) {
        User user = db.getUser(email);
        return user;
    }

    public String getUserID(String email) {
        User user = db.getUser(email);
        return user.getId();
    }



    public String getUserEmail() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");
        if (!email.isEmpty()) {
            return email;
        } else {
            return null;
        }
    }

    private String getCurrentOriginalPassword() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "No Email");
        DBHelper db = new DBHelper(getContext());
        User user = db.getUser(email);
        if (user != null) {
            return user.getOriginal();
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
    private Boolean CheckPasswordExistance(String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");
        DBHelper db = new DBHelper(getContext());
        String user = db.returnpasswordbyemail(email,password);
        if (user != null) {
            return true;
        } else {
            return false;
        }

    }
}

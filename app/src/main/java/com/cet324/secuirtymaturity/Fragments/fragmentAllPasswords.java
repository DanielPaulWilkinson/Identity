package com.cet324.secuirtymaturity.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.Adapters.PasswordCursorAdapter;
import com.cet324.secuirtymaturity.Database.DBHelper;
import com.cet324.secuirtymaturity.ObjectClasses.User;
import com.cet324.secuirtymaturity.PasswordSecurity.Encryption;
import com.cet324.secuirtymaturity.R;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class fragmentAllPasswords extends ListFragment {
    private  View myView;
    private  DBHelper db;
    private  Encryption en;
    private CursorAdapter cursorAdapter = null;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("All Passwords");
        myView = inflater.inflate(R.layout.fragment_all_passwords, container, false);
        db = new DBHelper(getContext());
        en = new Encryption();
        if(!isLoggedIn()){
            Fragment fragment = new fragmentLogin();
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
                final int moveToPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true)
                        .setMessage("Please select an option or return")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor cursor = (Cursor)getListView().getAdapter().getItem(moveToPosition);
                                String getPassID = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_ID)));
                                db.deletePassword(getPassID);
                                RefreshList();
                            }
                        })
                        .setNeutralButton("Return", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                return true;
            }


        });
        if(isLoggedIn()){

    //showing authentication dialog box
        //build the box
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //get the context of the layer
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //get the security layer
        View layout = inflater.inflate(R.layout.fragment_security, null);
        //add this layer to the screen
        builder.setView(layout);
        //get the text box inside of the box
        final EditText userpasswordinput = (EditText) layout.findViewById(R.id.txtPass);
        //make it so the user cannot close the box
        builder.setCancelable(false)
                //show message
                .setMessage("Please Enter Password to continue...")
                //set boix buttons
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        User user = db.getUser(getUserEmail());
                        String key = String.valueOf(user.getKey());
                        String salt = String.valueOf(user.getSalt());
                        String passwordAttempt = en.makeUnusable(userpasswordinput.getText().toString().trim(), key, salt);
                        if (passwordAttempt.equals(user.getPass())) {

                            //alert user of victory.
                            Toast.makeText(getContext(), "Authenticated", Toast.LENGTH_LONG).show();
                            //refresh the list so that the passwords now show.
                            RefreshList();
                            //else send the user to home screen
                        } else {
                            //get home page
                            Fragment fragment = new fragmentHome();
                            //get manager of pages
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            //start to move to the page
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            //replace this page with the new fragment
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            //move the page
                            fragmentTransaction.commit();
                        }
                    }
                })
                //when a negative button is clicked send them to home screen (optional: create class for fragment changes)
                .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment = new fragmentHome();
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.commit();
                    }
                })
                .show();
        }

    }
    public void RefreshList(){
        Cursor c = db.queryDatabase(DBHelper.TABLE_PASSWORDS,DBHelper.ALL_COLUMNS_PASSWORD, DBHelper.PASSWORD_EMAIL+" = " +"'"+String.valueOf(getUserEmail())+"'", null, null,DBHelper.PASSWORD_DATE + " ASC ", null);
        cursorAdapter = new PasswordCursorAdapter(getActivity(), c, 0);
        setListAdapter(cursorAdapter);
    }
    public void onListItemClick(ListView l, View view, int position, long id) {
        Cursor cursor = (Cursor)l.getAdapter().getItem(position);
        cursor.moveToPosition(position);
        String getPasswordID = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_ID)));
        movefragment(getPasswordID);
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
    public void movefragment(String getPasswordID) {
        Fragment fragment = new fragmentAllPasswordDetail();
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        bundle.putString("ID", getPasswordID);
        fragmentTransaction.addToBackStack(null);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_draw, menu);

    }
    public String getUserEmail(){
        //get saved data from shared prefs
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShaPreferences", getActivity().MODE_PRIVATE);
        //create local variable from the email
        String email = sharedPreferences.getString("Email","");
        //if not null return it
        if(email != null){
            return email;
        }
        else{
            return null;
        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_password) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentAddPassword()).addToBackStack(null).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
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

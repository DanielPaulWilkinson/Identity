package com.cet324.secuirtymaturity.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cet324.secuirtymaturity.Activities.MainActivity;
import com.cet324.secuirtymaturity.R;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class fragmentHelp extends Fragment {
   private View myView;
   private TextView txtDesc;
   private Spinner spinList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Help");
        myView = inflater.inflate(R.layout.fragment_help, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtDesc = (TextView) myView.findViewById(R.id.txtDesc);
        spinList = (Spinner) myView.findViewById(R.id.spinList);
        String[] values =
                {"1. What does this app do?",
                        "2. How do I find my passwords?",
                        "3. What do i need to remember?",
                        "4. How do i know if a password is good enough?",
                        "5. Can i reset my password, key or other user information?",
                        "6. Can i edit my passwords?",
                        "7. Can i delete passwords or my account?",
                        "8. How does the encoding work?",
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinList.setAdapter(adapter);
        spinList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if the id is selected show the corresponding answer
                if (spinList.getSelectedItemId() == 0) {
                    txtDesc.setText("This application employs security techniques in order to create, store and manage your passwords.");
                }
                if (spinList.getSelectedItemId() == 1) {
                    txtDesc.setText("Click All passwords and enter your application password to view all passwords stored within this application.");
                }
                if (spinList.getSelectedItemId() == 2) {
                    txtDesc.setText("When you register you create an encryption key and a password, remember them both to access all of the functionality within the software.");
                }
                if (spinList.getSelectedItemId() == 3) {
                    txtDesc.setText("You get a password strength check from the password creator.");
                }
                if (spinList.getSelectedItemId() == 4) {
                    txtDesc.setText("Yes, click on settings located both in the menu and home screen.");
                }
                if (spinList.getSelectedItemId() == 5) {
                    txtDesc.setText("Yes, head on over to the detail page from the all pasword section, be sure to decode the password before hand.");
                }
                if (spinList.getSelectedItemId() == 6) {
                    txtDesc.setText("Yes, you can delete these from the settings menu.");
                }
                if (spinList.getSelectedItemId() == 7) {
                    txtDesc.setText("Its a secret.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}

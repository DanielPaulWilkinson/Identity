package com.cet324.secuirtymaturity.Activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cet324.secuirtymaturity.Fragments.demo;
import com.cet324.secuirtymaturity.Fragments.fragmentAllPasswords;
import com.cet324.secuirtymaturity.Fragments.fragmentHelp;
import com.cet324.secuirtymaturity.Fragments.fragmentHome;
import com.cet324.secuirtymaturity.Fragments.fragmentLogin;
import com.cet324.secuirtymaturity.Fragments.fragmentPasswordCreator;
import com.cet324.secuirtymaturity.Fragments.fragmentSettings;
import com.cet324.secuirtymaturity.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
  private  SharedPreferences sharedPreferences;
  private  Menu menuNav;
    private  MenuItem element;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(isLoggedIn()) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentHome()).addToBackStack(null).commit();
        }else{
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentLogin()).addToBackStack(null).commit();
        }
        menuNav = navigationView.getMenu();
        element = menuNav.findItem(R.id.menu_account);

        if(isLoggedIn()){
            element.setTitle("Logout");
        }else{
            element.setTitle("Login");
        }


    }public Boolean isLoggedIn(){
        sharedPreferences = getApplicationContext().getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        String email= sharedPreferences.getString("Email", "");
        if(email.equals("") || email.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    @Override
    //when back is pressed if draw open close first
    //else then return to the previous fragment or activity in that stack.
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager =  getFragmentManager();
        int backCount = fragmentManager.getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START) & backCount > 1) {
            drawer.closeDrawer(GravityCompat.START);
            super.onBackPressed();
        }
        else if(backCount > 1){
            super.onBackPressed();
        }
        else{
            finish();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentHome()).addToBackStack(null).commit();
        } else if (id == R.id.nav_all) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentAllPasswords()).addToBackStack(null).commit();
        } else if (id == R.id.nav_help) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentHelp()).addToBackStack(null).commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentSettings()).addToBackStack(null).commit();
        } else if (id == R.id.nav_passcreator) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentPasswordCreator()).addToBackStack(null).commit();
        } else if (id == R.id.menu_account) {
            sharedPreferences = getApplicationContext().getSharedPreferences("CET300", Context.MODE_PRIVATE);
            if (isLoggedIn()) {
                sharedPreferences.edit().putString("Email", "").apply();
                Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentHome()).addToBackStack(null).commit();
                element.setTitle("Login");
            } else {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new fragmentLogin()).addToBackStack(null).commit();
                element.setTitle("Login");
            }
        }  else if (id == R.id.menu_demo) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new demo()).addToBackStack(null).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.milan.bataochatapplication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.google.firebase.database.DataSnapshot;

public class SharedPrefHelper {

    private static final String PREFNAME = "userdata_student";
    Context context;

    public SharedPrefHelper(Context context){
        this.context = context;
    }


    public void storeUserData(String firstname, String lastname, String gender, String email, String photo, String type,
                              String username, String phone, String state, String status, String desc,
                              String friendsCount){

        SharedPreferences sharedPref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("gender", gender);
        editor.putString("email", email);
        editor.putString("photo", photo);
        editor.putString("username", username);
        editor.putString("phone", phone);
        editor.putString("state", state);
        editor.putString("desc", desc);
        editor.putString("status", status);
        editor.putString("friends_count", friendsCount);
        editor.putString("type", type);
        editor.commit();

    }

    public void storeUserDataOnSignUp(String firstname, String lastname, String gender, String email, String type, String username, String phone, String state){

        SharedPreferences sharedPref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("gender", gender);
        editor.putString("email", email);
        editor.putString("username", username);
        editor.putString("phone", phone);
        editor.putString("state", state);
        editor.putString("type", type);
        editor.commit();

    }

    public void setStudentProperty(String property, String value){

        SharedPreferences sharedPref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(property, value);
        editor.apply();

    }


    public void clearData(){

        SharedPreferences sharedPref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }


    public String getUserData(String property){

        SharedPreferences sharedPref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        return sharedPref.getString(property, null);

    }



    public void saveUserProfile(DataSnapshot dataSnapshot, String email) {

        String firstname = (String) dataSnapshot.child("firstname").getValue();
        String lastname = (String) dataSnapshot.child("lastname").getValue();
        String gender = (String) dataSnapshot.child("gender").getValue();
        String photo = (String) dataSnapshot.child("photo").getValue();
        String geender = (String) dataSnapshot.child("gender").getValue();
        String type = (String) dataSnapshot.child("type").getValue();
        String username = (String) dataSnapshot.child("username").getValue();
        String phone = (String) dataSnapshot.child("phone").getValue();
        String state = (String) dataSnapshot.child("state").getValue();
        String status = (String) dataSnapshot.child("status").getValue();
        String desc = (String) dataSnapshot.child("desc").getValue();
        String friendsCount = (String) dataSnapshot.child("friends_count").getValue();
        storeUserData(firstname, lastname, gender, email, photo, type, username, phone, state, status, desc, friendsCount);
        PackageManager pm  = context.getPackageManager();
        ComponentName componentName=new ComponentName(context.getPackageName(),context.getPackageName()+".user.SendToActivity");
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}

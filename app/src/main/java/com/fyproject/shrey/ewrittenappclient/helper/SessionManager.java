package com.fyproject.shrey.ewrittenappclient.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;

/**
 * Created by shrey on 02/03/17.
 */

public class SessionManager {
    SharedPreferences pref;
    Context context;
    SharedPreferences.Editor editor;

    private static final String PREF_FILE = "com.fyproject.shrey.ewrittenapp.USER_INFO";
    private static final String KEY_USER_TYPE="userType";
    private static boolean isCurrentUserSet;

    public SessionManager(Context context){
        this.context=context;
        pref=context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        editor=pref.edit();
        editor.commit();
    }

    public void setCurrentUser(StudentProfile user,String userType){
        editor.putString(KEY_USER_TYPE,userType);

        editor.putString("Uid",user.getUid());
        editor.putString("fname",user.fname);
        editor.putString("mname",user.mname);
        editor.putString("lname",user.lname);
        editor.putString("email",user.email);
        editor.putString("enroll",user.enroll);
        editor.putString("mobile",user.mobile);
        editor.putString("branch",user.branch);
        editor.putString("div",user.div);
        editor.putString("sem",user.sem);

        isCurrentUserSet=true;
        editor.apply();
    }

    public StudentProfile getCurrentUser(){
        StudentProfile student = new StudentProfile();

        student.fname=pref.getString("fname","NULL");
        student.lname=pref.getString("lname","NULL");
        student.mname=pref.getString("fname","NULL");
        student.email=pref.getString("email","NULL");
        student.enroll=pref.getString("enroll","NULL");
        student.mobile=pref.getString("mobile","NULL");
        student.branch=pref.getString("branch","NULL");
        student.div=pref.getString("div","NULL");
        student.sem=pref.getString("sem","NULL");
        student.Uid=pref.getString("Uid","NULL");

        return student;
    }


    public void setUserType(String user){
        editor.putString(KEY_USER_TYPE,user);
        editor.commit();
    }

    public String getUserType(){
        String defaultValue="null";
        return pref.getString(KEY_USER_TYPE,defaultValue);
    }

    public void ClearUserType(){
        editor.remove(KEY_USER_TYPE);
        editor.commit();
    }




}

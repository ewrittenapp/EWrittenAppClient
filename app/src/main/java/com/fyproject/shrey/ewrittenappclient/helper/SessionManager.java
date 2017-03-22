package com.fyproject.shrey.ewrittenappclient.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.model.FacultyProfile;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.UserProfile;

/**
 * Created by shrey on 02/03/17.
 */

public class SessionManager {
    private SharedPreferences pref;
    private Context context;
    private SharedPreferences.Editor editor;

    private static final String PREF_FILE = "com.fyproject.shrey.ewrittenapp.USER_INFO";
    private static final String KEY_USER_TYPE="userType";
    private static boolean isCurrentUserSet;

    public SessionManager(Context context){
        this.context=context;
        pref=context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        editor=pref.edit();
        editor.commit();
    }

    //save student data
    public void setCurrentUser(StudentProfile user,String userType){
        editor=pref.edit();

        Log.d("TAG", "setCurrentUser: CODE EXECUTED");
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
        Log.d("TAG", "setCurrentUser: "+pref.getString(KEY_USER_TYPE,"no userType"));
        editor.commit();
    }

    //save faculty data
    public void setCurrentUser(FacultyProfile user, String userType){
        editor.putString(KEY_USER_TYPE,userType);

        editor.putString("Uid",user.getUid());
        editor.putString("name",user.name);
        editor.putString("email",user.email);
        editor.putString("mobile",user.mobile);
        isCurrentUserSet=true;
        editor.commit();
    }

    //get student data
    public UserProfile getCurrentUser(){
        String userType=getUserType();

        if(userType.equals(context.getString(R.string.student))) {
            StudentProfile student = new StudentProfile();
            student.fname = pref.getString("fname", "NULL");
            student.lname = pref.getString("lname", "NULL");
            student.mname = pref.getString("fname", "NULL");
            student.email = pref.getString("email", "NULL");
            student.enroll = pref.getString("enroll", "NULL");
            student.mobile = pref.getString("mobile", "NULL");
            student.branch = pref.getString("branch", "NULL");
            student.div = pref.getString("div", "NULL");
            student.sem = pref.getString("sem", "NULL");
            student.Uid = pref.getString("Uid", "NULL");
            return student ;
        } else if (userType.equals(context.getString(R.string.faculty))) {
            FacultyProfile fp = new FacultyProfile();
            fp.branch = pref.getString("branch", "NULL");
            fp.name = pref.getString("name", "NULL");
            fp.email = pref.getString("email", "NULL");
            fp.mobile = pref.getString("mobile", "NULL");
            fp.Uid = pref.getString("Uid", "NULL");
            return fp;
        }

        Log.d("TAG", "SessionManager.getCurrentUser: UserType not found");
        return null;//no userType found
    }



    public void clearCurrentUser(){
        String userType=getUserType();

        if(userType.equals(context.getString(R.string.student))){
            editor.remove(KEY_USER_TYPE);
            editor.remove("fname");
            editor.remove("mname");
            editor.remove("lname");
            editor.remove("email");
            editor.remove("enroll");
            editor.remove("mobile");
            editor.remove("branch");
            editor.remove("div");
            editor.remove("sem");
            editor.remove("Uid");
        }else if(userType.equals(context.getString(R.string.faculty))){
            editor.remove(KEY_USER_TYPE);
            editor.remove("name");
            editor.remove("email");
            editor.remove("enroll");
            editor.remove("mobile");
        }
        editor.commit();
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

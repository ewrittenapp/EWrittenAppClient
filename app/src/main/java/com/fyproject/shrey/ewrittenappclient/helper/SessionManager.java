package com.fyproject.shrey.ewrittenappclient.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shrey on 02/03/17.
 */

public class SessionManager {
    SharedPreferences pref;
    Context context;
    SharedPreferences.Editor editor;

    private static final String PREF_FILE = "com.fyproject.shrey.ewrittenapp.USER_INFO";
    private static final String KEY_USER_TYPE="userType";

    public SessionManager(Context context){
        this.context=context;
        pref=context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        editor=pref.edit();
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

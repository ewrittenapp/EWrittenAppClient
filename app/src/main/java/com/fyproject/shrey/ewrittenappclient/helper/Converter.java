package com.fyproject.shrey.ewrittenappclient.helper;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shrey on 13/03/17.
 */

public class Converter {

    String TAG="TAG";
    public Map<String,String> sem = new HashMap<>();
    public Map<String,String> Class = new HashMap<>();

    public Converter(){
        //init sem
        for (int i = 1; i <= 8; i++) {
            sem.put(Integer.toString(i), "Sem"+i);
        }
        //init Class
        Class.put("A","ClassA");
        Class.put("B","ClassB");
    }

    public String convertBranch(String branch){
        return branch.replaceAll("\\s","");
    }


    public String convertSem(String semNumber){
        if(sem.get(semNumber)==null)
            Log.d(TAG, "Converter.convertSem: returned null, key not found.");

        return sem.get(semNumber);
    }

    public String convertClass(String studentProfile_div){
        if(Class.get(studentProfile_div)==null)
           Log.d(TAG, "Converter.convertClass: returned null, key not found.");

        return Class.get(studentProfile_div);
    }

}

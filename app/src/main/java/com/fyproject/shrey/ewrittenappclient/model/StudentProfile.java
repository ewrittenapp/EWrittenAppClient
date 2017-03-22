package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shrey on 08/03/17.
 */
@IgnoreExtraProperties
public class StudentProfile extends UserProfile{
    public String fname;
    public String lname;
    public String mname;
   // public String email;
    public String enroll;
    public String mobile;
    public String branch;
    public String div;
    public String sem;
 //   @Exclude public String Uid;

    public StudentProfile(){
    }

    @Exclude
    public void setUid(String id){
        Uid=id;
    }
    @Exclude
    public String getUid(){
        return Uid;
    }

}

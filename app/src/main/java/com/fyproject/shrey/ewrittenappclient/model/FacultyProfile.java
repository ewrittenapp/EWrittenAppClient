package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;

/**
 * Created by shrey on 20/03/17.
 */

public class FacultyProfile extends UserProfile{
    public String name;
   // public String email;
    public String mobile;
    public String branch;
  //  @Exclude public String Uid;

    public FacultyProfile(){
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

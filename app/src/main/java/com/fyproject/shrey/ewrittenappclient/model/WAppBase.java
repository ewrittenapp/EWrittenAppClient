package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;

/**
 * Created by shrey on 13/03/17.
 */

public class WAppBase {
    public String type;
    public String toUid;
    public String toName;
    public String fromUid;
    public String fromName;
    public String date_submitted;
    public String status;
    @Exclude public String wAppId;

    public WAppBase(){
    }

    public WAppBase(String type,String toUid,String toName,String fromUid,String fromName,String date_submitted,String status){

    }

    @Exclude
    public boolean allDataSet(){
        if(type==null || toName==null || toUid==null || fromName==null || fromUid==null || date_submitted==null || status==null)
            return false;

        return true;
    }


}

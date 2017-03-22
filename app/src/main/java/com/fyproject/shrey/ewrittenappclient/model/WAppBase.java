package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by shrey on 13/03/17.
 */

public class WAppBase implements Serializable {
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
    public String getwAppId(){
        return wAppId;
    }
    @Exclude
    public void setwAppId(String appId){
        this.wAppId=appId;
    }

    public String getToName() {
        return toName;
    }
    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getDate_submitted() {
        return date_submitted;
    }
    public void setDate_submitted(String date_submitted) {
        this.date_submitted = date_submitted;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String str="toName: "+toName+" | type: "+ type +" | date_submitted: "+ date_submitted +" | status: "+status;
        return str;
    }

    @Exclude
    public boolean allDataSet(){
        if(type==null || toName==null || toUid==null || fromName==null || fromUid==null || date_submitted==null || status==null)
            return false;

        return true;
    }


}

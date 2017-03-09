package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shrey on 06/03/17.
 */

@IgnoreExtraProperties
public class rvStudentRow {

    public String toName;
    public String appType;
    public String sentDate;
    public String status;

    @Exclude String wAppId;

    public rvStudentRow(){
    }

    public  rvStudentRow(String toName,String appType,String date,String status){
        this.toName = toName;
        this.appType = appType;
        this.sentDate = date;
        this.status= status;
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

    public String getAppType() {
        return appType;
    }
    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getSentDate() {
        return sentDate;
    }
    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String str="toName: "+toName+" | appType: "+appType+" | sentDate: "+ sentDate +" | status: "+status;
        return str;
    }
}

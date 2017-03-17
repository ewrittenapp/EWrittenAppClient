package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shrey on 06/03/17.
 */

@IgnoreExtraProperties
public class rvStudentRow {

    public String toName;
    public String type;
    public String date_submitted;
    public String status;
    @Exclude public String wAppId;

    public rvStudentRow(){
    }

    public  rvStudentRow(String toName,String appType,String date,String status){
        this.toName = toName;
        this.type = appType;
        this.date_submitted = date;
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
}

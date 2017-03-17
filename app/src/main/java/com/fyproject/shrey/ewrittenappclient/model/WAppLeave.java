package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by shrey on 13/03/17.
 Base class properties:
 public String type;
 public String toUid;
 public String toName;
 public String fromUid;
 public String fromName;
 public String date_submitted;
 public String status;
 Exclude String wAppId;
 */

@IgnoreExtraProperties
public class WAppLeave extends WAppBase {

    public String enroll;
    public String classInfo;
    public String message;
    public String startDate;
    public String endDate;
    public String response="null";
    public String attachedFile="null";

    public WAppLeave(){
    }

    public WAppLeave(StudentProfile sp){
        fromUid=sp.getUid();
        fromName=sp.fname+" "+sp.lname;
        enroll=sp.enroll;
        classInfo=sp.branch+" semester "+sp.sem+" class: "+sp.div;

        type="Leave Application";
        date_submitted= DateFormat.getDateInstance().format(new Date());
        status="pending";
    }


}

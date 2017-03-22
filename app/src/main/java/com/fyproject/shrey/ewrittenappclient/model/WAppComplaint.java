package com.fyproject.shrey.ewrittenappclient.model;

import android.content.Context;

import com.fyproject.shrey.ewrittenappclient.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by shrey on 22/03/17.
 */

public class WAppComplaint extends WAppBase {

    public String enroll;
    public String classInfo;

    public String complaintType;
    public String message;
    public String level;
    public String response="null";
    public String attachedFile="null";

    public WAppComplaint(){
    }

    public WAppComplaint(StudentProfile sp, Context x){
        fromUid=sp.getUid();
        fromName=sp.fname+" "+sp.lname;
        enroll=sp.enroll;
        classInfo=sp.branch+" semester "+sp.sem+" class: "+sp.div;

        type=x.getString(R.string.complaint);
        date_submitted= DateFormat.getDateInstance().format(new Date());
        status="pending";
    }

}

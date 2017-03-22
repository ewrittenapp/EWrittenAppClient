package com.fyproject.shrey.ewrittenappclient.model;

import android.content.Context;

import com.fyproject.shrey.ewrittenappclient.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by shrey on 22/03/17.
 */

public class WAppOrganizeEvent extends WAppBase {
    public String enroll;
    public String classInfo;

    public String eventName;
    public String message;
    public String startDate;
    public String endDate;
    public String response="null";
    public String attachedFile="null";

    public WAppOrganizeEvent(){
    }

    public WAppOrganizeEvent(StudentProfile sp, Context x){
        fromUid=sp.getUid();
        fromName=sp.fname+" "+sp.lname;
        enroll=sp.enroll;
        classInfo=sp.branch+" semester "+sp.sem+" class: "+sp.div;

        type=x.getString(R.string.organize_event);
        date_submitted= DateFormat.getDateInstance().format(new Date());
        status="pending";
    }

}

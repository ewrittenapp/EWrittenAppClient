package com.fyproject.shrey.ewrittenappclient.model;

import android.content.Context;

import com.fyproject.shrey.ewrittenappclient.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by shrey on 21/03/17.
 */

public class WAppBonafide extends WAppBase {

    public String enroll;
    public String classInfo;
    public String message;
    public String response="null";
    public String attachedFile="null";

    public WAppBonafide(){
    }

    public WAppBonafide(StudentProfile sp, Context x){
        fromUid=sp.getUid();
        fromName=sp.fname+" "+sp.lname;
        enroll=sp.enroll;
        classInfo=sp.branch+" semester "+sp.sem+" class: "+sp.div;

        type=x.getString(R.string.bonafide);
        date_submitted= DateFormat.getDateInstance().format(new Date());
        status="pending";
    }

}
